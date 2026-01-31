package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.domain.entity.PageRequest;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.enums.RepairActionTypeEnum;
import com.rosy.common.enums.RepairOrderStatusEnum;
import com.rosy.common.enums.RepairNotifyTypeEnum;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.dto.RepairOrderAssignRequest;
import com.rosy.main.domain.dto.RepairOrderSubmitRequest;
import com.rosy.main.domain.entity.RepairOrder;
import com.rosy.main.domain.entity.RepairStaff;
import com.rosy.main.domain.vo.RepairOrderVO;
import com.rosy.main.domain.vo.RepairEvaluationVO;
import com.rosy.main.mapper.RepairOrderMapper;
import com.rosy.main.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RepairOrderServiceImpl extends ServiceImpl<RepairOrderMapper, RepairOrder> implements IRepairOrderService {

    private final IRepairStaffService repairStaffService;
    private final IRepairNotificationService notificationService;
    private final IRepairEvaluationService evaluationService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RepairOrderVO submitOrder(RepairOrderSubmitRequest request) {
        RepairOrder order = new RepairOrder();
        BeanUtil.copyProperties(request, order);
        order.setOrderNo(generateOrderNo());
        order.setStatus(RepairOrderStatusEnum.PENDING.getCode().byteValue());
        if (request.getPriority() == null) {
            order.setPriority((byte) 2);
        }
        save(order);
        return convertToVO(order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RepairOrderVO assignOrder(RepairOrderAssignRequest request) {
        RepairOrder order = getById(request.getOrderId());
        if (order == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "工单不存在");
        }
        if (!order.getStatus().equals(RepairOrderStatusEnum.PENDING.getCode().byteValue())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "工单状态不允许分配");
        }
        
        RepairStaff staff = repairStaffService.getById(request.getStaffId());
        if (staff == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "维修人员不存在");
        }
        
        order.setStaffId(request.getStaffId());
        order.setAssignType((byte) 1);
        order.setAssignTime(LocalDateTime.now());
        order.setStatus(RepairOrderStatusEnum.ASSIGNED.getCode().byteValue());
        if (request.getPriority() != null) {
            order.setPriority(request.getPriority());
        }
        updateById(order);
        
        staff.setOrderCount(staff.getOrderCount() + 1);
        repairStaffService.updateById(staff);
        
        notificationService.sendNotification(
            order.getId(),
            staff.getUserId(),
            RepairNotifyTypeEnum.ORDER_ASSIGN.getCode().byteValue(),
            "工单分配通知",
            "您有新的工单需要处理，工单编号：" + order.getOrderNo()
        );
        
        return convertToVO(order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RepairOrderVO autoAssignOrder(Long orderId) {
        RepairOrder order = getById(orderId);
        if (order == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "工单不存在");
        }
        
        RepairStaff staff = repairStaffService.getAvailableStaffBySpecialty(order.getFaultType());
        if (staff == null) {
            staff = repairStaffService.getAvailableStaffWithLeastOrders();
        }
        if (staff == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "暂无可用维修人员");
        }
        
        order.setStaffId(staff.getId());
        order.setAssignType((byte) 0);
        order.setAssignTime(LocalDateTime.now());
        order.setStatus(RepairOrderStatusEnum.ASSIGNED.getCode().byteValue());
        updateById(order);
        
        staff.setOrderCount(staff.getOrderCount() + 1);
        repairStaffService.updateById(staff);
        
        notificationService.sendNotification(
            order.getId(),
            staff.getUserId(),
            RepairNotifyTypeEnum.ORDER_ASSIGN.getCode().byteValue(),
            "工单分配通知",
            "系统自动为您分配了新工单，工单编号：" + order.getOrderNo()
        );
        
        return convertToVO(order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RepairOrderVO acceptOrder(Long orderId) {
        RepairOrder order = getById(orderId);
        if (order == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "工单不存在");
        }
        if (!order.getStatus().equals(RepairOrderStatusEnum.ASSIGNED.getCode().byteValue())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "工单状态不允许接单");
        }
        
        order.setStatus(RepairOrderStatusEnum.REPAIRING.getCode().byteValue());
        order.setAcceptTime(LocalDateTime.now());
        long responseMinutes = ChronoUnit.MINUTES.between(order.getCreateTime(), order.getAcceptTime());
        order.setResponseMinutes((int) responseMinutes);
        updateById(order);
        
        notificationService.sendNotification(
            order.getId(),
            order.getUserId(),
            RepairNotifyTypeEnum.STATUS_CHANGE.getCode().byteValue(),
            "工单状态更新",
            "您的工单已被维修人员接单，正在维修中"
        );
        
        return convertToVO(order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RepairOrderVO completeOrder(Long orderId) {
        RepairOrder order = getById(orderId);
        if (order == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "工单不存在");
        }
        if (!order.getStatus().equals(RepairOrderStatusEnum.REPAIRING.getCode().byteValue())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "工单状态不允许完成");
        }
        
        order.setStatus(RepairOrderStatusEnum.CONFIRMING.getCode().byteValue());
        order.setCompletedTime(LocalDateTime.now());
        updateById(order);
        
        RepairStaff staff = repairStaffService.getById(order.getStaffId());
        if (staff != null && staff.getOrderCount() > 0) {
            staff.setOrderCount(staff.getOrderCount() - 1);
            repairStaffService.updateById(staff);
        }
        
        notificationService.sendNotification(
            order.getId(),
            order.getUserId(),
            RepairNotifyTypeEnum.REPAIR_COMPLETE.getCode().byteValue(),
            "维修完成通知",
            "您的工单已完成维修，请确认并评价"
        );
        
        return convertToVO(order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RepairOrderVO cancelOrder(Long orderId) {
        RepairOrder order = getById(orderId);
        if (order == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "工单不存在");
        }
        if (order.getStatus() >= RepairOrderStatusEnum.REPAIRING.getCode()) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "工单已开始处理，无法取消");
        }
        
        order.setStatus(RepairOrderStatusEnum.CANCELLED.getCode().byteValue());
        updateById(order);
        
        if (order.getStaffId() != null) {
            RepairStaff staff = repairStaffService.getById(order.getStaffId());
            if (staff != null && staff.getOrderCount() > 0) {
                staff.setOrderCount(staff.getOrderCount() - 1);
                repairStaffService.updateById(staff);
            }
        }
        
        return convertToVO(order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RepairOrderVO confirmCompletion(Long orderId) {
        RepairOrder order = getById(orderId);
        if (order == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "工单不存在");
        }
        if (!order.getStatus().equals(RepairOrderStatusEnum.CONFIRMING.getCode().byteValue())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "工单状态不允许确认");
        }
        
        order.setStatus(RepairOrderStatusEnum.COMPLETED.getCode().byteValue());
        updateById(order);
        
        notificationService.sendNotification(
            order.getId(),
            order.getUserId(),
            RepairNotifyTypeEnum.EVALUATION_REMIND.getCode().byteValue(),
            "评价提醒",
            "请对本次维修服务进行评价"
        );
        
        return convertToVO(order);
    }

    @Override
    public RepairOrderVO getOrderDetail(Long orderId) {
        RepairOrder order = getById(orderId);
        if (order == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "工单不存在");
        }
        return convertToVO(order);
    }

    @Override
    public Page<RepairOrderVO> getMyOrders(PageRequest pageRequest) {
        Page<RepairOrder> page = new Page<>(pageRequest.getCurrent(), pageRequest.getPageSize());
        LambdaQueryWrapper<RepairOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(RepairOrder::getCreateTime);
        Page<RepairOrder> orderPage = page(page, wrapper);
        return (Page<RepairOrderVO>) orderPage.convert(this::convertToVO);
    }

    @Override
    public Page<RepairOrderVO> getStaffOrders(PageRequest pageRequest) {
        Page<RepairOrder> page = new Page<>(pageRequest.getCurrent(), pageRequest.getPageSize());
        LambdaQueryWrapper<RepairOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(RepairOrder::getCreateTime);
        Page<RepairOrder> orderPage = page(page, wrapper);
        return (Page<RepairOrderVO>) orderPage.convert(this::convertToVO);
    }

    @Override
    public Page<RepairOrderVO> getAllOrders(PageRequest pageRequest) {
        Page<RepairOrder> page = new Page<>(pageRequest.getCurrent(), pageRequest.getPageSize());
        LambdaQueryWrapper<RepairOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(RepairOrder::getCreateTime);
        Page<RepairOrder> orderPage = page(page, wrapper);
        return (Page<RepairOrderVO>) orderPage.convert(this::convertToVO);
    }

    private String generateOrderNo() {
        return "RO" + System.currentTimeMillis() + IdUtil.nanoId(6);
    }

    private RepairOrderVO convertToVO(RepairOrder order) {
        RepairOrderVO vo = BeanUtil.copyProperties(order, RepairOrderVO.class);
        vo.setStatusDesc(RepairOrderStatusEnum.getDescByCode(order.getStatus().intValue()));
        vo.setPriorityDesc(Optional.ofNullable(order.getPriority())
            .map(p -> com.rosy.common.enums.RepairPriorityEnum.getDescByCode(p.intValue()))
            .orElse(null));
        vo.setAssignTypeDesc(order.getAssignType() != null ? (order.getAssignType() == 0 ? "自动分配" : "手动分配") : null);
        
        if (order.getStaffId() != null) {
            RepairStaff staff = repairStaffService.getById(order.getStaffId());
            if (staff != null) {
                vo.setStaffName(staff.getStaffName());
                vo.setStaffPhone(staff.getPhone());
            }
        }
        
        RepairEvaluationVO evaluation = evaluationService.getEvaluationByOrderId(order.getId());
        vo.setHasEvaluation(evaluation != null);
        vo.setEvaluation(evaluation);
        
        return vo;
    }
}
