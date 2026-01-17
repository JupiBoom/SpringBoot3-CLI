package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.QueryWrapperUtil;
import com.rosy.main.domain.dto.repair.RepairOrderAddRequest;
import com.rosy.main.domain.dto.repair.RepairOrderAssignRequest;
import com.rosy.main.domain.dto.repair.RepairOrderCompleteRequest;
import com.rosy.main.domain.dto.repair.RepairOrderQueryRequest;
import com.rosy.main.domain.entity.*;
import com.rosy.main.domain.vo.repair.RepairEvaluationVO;
import com.rosy.main.domain.vo.repair.RepairOrderVO;
import com.rosy.main.domain.vo.repair.RepairRecordVO;
import com.rosy.main.enums.*;
import com.rosy.main.mapper.RepairOrderMapper;
import com.rosy.main.service.*;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class RepairOrderServiceImpl extends ServiceImpl<RepairOrderMapper, RepairOrder> implements IRepairOrderService {

    @Resource
    private IRepairPhotoService repairPhotoService;

    @Resource
    private IRepairRecordService repairRecordService;

    @Resource
    private IRepairEvaluationService repairEvaluationService;

    @Resource
    private IRepairPersonnelService repairPersonnelService;

    @Resource
    private INotificationService notificationService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RepairOrderVO createRepairOrder(RepairOrderAddRequest request, Long userId) {
        RepairOrder repairOrder = BeanUtil.copyProperties(request, RepairOrder.class);
        repairOrder.setOrderNo(generateOrderNo());
        repairOrder.setStatus(RepairOrderStatusEnum.PENDING.getCode().byteValue());
        repairOrder.setReporterId(userId);
        boolean saved = save(repairOrder);
        if (!saved) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "创建报修单失败");
        }

        if (request.getPhotoUrls() != null && !request.getPhotoUrls().isEmpty()) {
            repairPhotoService.savePhotos(repairOrder.getId(), request.getPhotoUrls(), PhotoTypeEnum.FAULT.getCode().byteValue(), userId);
        }

        return getRepairOrderVO(repairOrder.getId());
    }

    @Override
    public RepairOrderVO getRepairOrderVO(Long orderId) {
        RepairOrder repairOrder = getById(orderId);
        if (repairOrder == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "报修单不存在");
        }

        RepairOrderVO vo = BeanUtil.copyProperties(repairOrder, RepairOrderVO.class);

        RepairOrderStatusEnum statusEnum = RepairOrderStatusEnum.getByCode(repairOrder.getStatus().intValue());
        vo.setStatusDesc(statusEnum != null ? statusEnum.getDesc() : "");

        PriorityEnum priorityEnum = PriorityEnum.getByCode(repairOrder.getPriority().intValue());
        vo.setPriorityDesc(priorityEnum != null ? priorityEnum.getDesc() : "");

        if (repairOrder.getAssigneeId() != null) {
            RepairPersonnel personnel = repairPersonnelService.getById(repairOrder.getAssigneeId());
            if (personnel != null) {
                vo.setAssigneeName(personnel.getName());
            }
        }

        List<String> photoUrls = repairPhotoService.getPhotoUrlsByOrderId(orderId);
        vo.setPhotoUrls(photoUrls);

        List<RepairRecordVO> records = repairRecordService.getRecordsByOrderId(orderId);
        vo.setRecords(records);

        RepairEvaluationVO evaluation = repairEvaluationService.getEvaluationByOrderId(orderId);
        vo.setEvaluation(evaluation);

        return vo;
    }

    @Override
    public Page<RepairOrderVO> pageRepairOrders(RepairOrderQueryRequest request) {
        long current = request.getCurrent();
        long size = request.getPageSize();
        Page<RepairOrder> page = page(new Page<>(current, size), getQueryWrapper(request));
        Page<RepairOrderVO> voPage = new Page<>(current, size, page.getTotal());
        voPage.setRecords(page.getRecords().stream()
                .map(order -> {
                    RepairOrderVO vo = BeanUtil.copyProperties(order, RepairOrderVO.class);
                    RepairOrderStatusEnum statusEnum = RepairOrderStatusEnum.getByCode(order.getStatus().intValue());
                    vo.setStatusDesc(statusEnum != null ? statusEnum.getDesc() : "");
                    PriorityEnum priorityEnum = PriorityEnum.getByCode(order.getPriority().intValue());
                    vo.setPriorityDesc(priorityEnum != null ? priorityEnum.getDesc() : "");
                    return vo;
                })
                .toList());
        return voPage;
    }

    @Override
    public LambdaQueryWrapper<RepairOrder> getQueryWrapper(RepairOrderQueryRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        LambdaQueryWrapper<RepairOrder> queryWrapper = new LambdaQueryWrapper<>();

        QueryWrapperUtil.addCondition(queryWrapper, request.getOrderNo(), RepairOrder::getOrderNo);
        QueryWrapperUtil.addCondition(queryWrapper, request.getDeviceType(), RepairOrder::getDeviceType);
        QueryWrapperUtil.addCondition(queryWrapper, request.getFaultType(), RepairOrder::getFaultType);
        QueryWrapperUtil.addCondition(queryWrapper, request.getPriority(), RepairOrder::getPriority);
        QueryWrapperUtil.addCondition(queryWrapper, request.getStatus(), RepairOrder::getStatus);
        QueryWrapperUtil.addCondition(queryWrapper, request.getReporterId(), RepairOrder::getReporterId);
        QueryWrapperUtil.addCondition(queryWrapper, request.getAssigneeId(), RepairOrder::getAssigneeId);

        if (StrUtil.isNotBlank(request.getStartTime())) {
            queryWrapper.ge(RepairOrder::getCreateTime, request.getStartTime());
        }
        if (StrUtil.isNotBlank(request.getEndTime())) {
            queryWrapper.le(RepairOrder::getCreateTime, request.getEndTime());
        }

        QueryWrapperUtil.addSortCondition(queryWrapper, request.getSortField(), request.getSortOrder(), RepairOrder::getId);

        return queryWrapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignOrder(RepairOrderAssignRequest request) {
        RepairOrder repairOrder = getById(request.getOrderId());
        if (repairOrder == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "报修单不存在");
        }
        if (!RepairOrderStatusEnum.PENDING.getCode().equals(repairOrder.getStatus().intValue())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "只能分配待处理的报修单");
        }

        RepairPersonnel personnel = repairPersonnelService.getById(request.getAssigneeId());
        if (personnel == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "维修人员不存在");
        }

        repairOrder.setAssigneeId(request.getAssigneeId());
        repairOrder.setStatus(RepairOrderStatusEnum.ASSIGNED.getCode().byteValue());
        repairOrder.setAssignTime(LocalDateTime.now());
        boolean updated = updateById(repairOrder);
        if (!updated) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "分配报修单失败");
        }

        notificationService.sendNotification(
                request.getAssigneeId(),
                personnel.getName(),
                "新的报修任务",
                "您有一个新的报修任务，单号：" + repairOrder.getOrderNo(),
                NotificationTypeEnum.ASSIGN.getCode().byteValue(),
                repairOrder.getId()
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void acceptOrder(Long orderId, Long userId) {
        RepairOrder repairOrder = getById(orderId);
        if (repairOrder == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "报修单不存在");
        }
        if (!RepairOrderStatusEnum.ASSIGNED.getCode().equals(repairOrder.getStatus().intValue())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "只能接单已分配的报修单");
        }
        if (!userId.equals(repairOrder.getAssigneeId())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "只能接单分配给自己的报修单");
        }

        repairOrder.setStatus(RepairOrderStatusEnum.REPAIRING.getCode().byteValue());
        repairOrder.setAcceptTime(LocalDateTime.now());
        boolean updated = updateById(repairOrder);
        if (!updated) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "接单失败");
        }

        repairRecordService.createRecord(
                new com.rosy.main.domain.dto.repair.RepairRecordAddRequest(),
                userId
        );

        notificationService.sendNotification(
                repairOrder.getReporterId(),
                repairOrder.getReporterName(),
                "报修进度更新",
                "您的报修单" + repairOrder.getOrderNo() + "已被接单，维修人员正在处理",
                NotificationTypeEnum.ACCEPT.getCode().byteValue(),
                repairOrder.getId()
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void completeOrder(RepairOrderCompleteRequest request) {
        RepairOrder repairOrder = getById(request.getOrderId());
        if (repairOrder == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "报修单不存在");
        }
        if (!RepairOrderStatusEnum.REPAIRING.getCode().equals(repairOrder.getStatus().intValue())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "只能完成维修中的报修单");
        }

        repairOrder.setStatus(RepairOrderStatusEnum.COMPLETED.getCode().byteValue());
        repairOrder.setRepairResult(request.getRepairResult());
        repairOrder.setCompleteTime(LocalDateTime.now());
        boolean updated = updateById(repairOrder);
        if (!updated) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "完成报修单失败");
        }

        repairRecordService.createRecord(
                new com.rosy.main.domain.dto.repair.RepairRecordAddRequest(),
                repairOrder.getAssigneeId()
        );

        repairPersonnelService.updatePersonnelStats(repairOrder.getAssigneeId(), true);

        notificationService.sendNotification(
                repairOrder.getReporterId(),
                repairOrder.getReporterName(),
                "报修完成通知",
                "您的报修单" + repairOrder.getOrderNo() + "已完成，请确认并进行评价",
                NotificationTypeEnum.COMPLETE.getCode().byteValue(),
                repairOrder.getId()
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrder(Long orderId, Long userId) {
        RepairOrder repairOrder = getById(orderId);
        if (repairOrder == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "报修单不存在");
        }
        if (RepairOrderStatusEnum.COMPLETED.getCode().equals(repairOrder.getStatus().intValue())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "已完成的报修单不能取消");
        }

        repairOrder.setStatus(RepairOrderStatusEnum.CANCELLED.getCode().byteValue());
        boolean updated = updateById(repairOrder);
        if (!updated) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "取消报修单失败");
        }

        if (repairOrder.getAssigneeId() != null) {
            repairPersonnelService.updatePersonnelStats(repairOrder.getAssigneeId(), false);
        }
    }

    @Override
    public String generateOrderNo() {
        return "BX" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + (int)(Math.random() * 1000);
    }
}
