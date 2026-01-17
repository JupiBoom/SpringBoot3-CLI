package com.rosy.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rosy.common.enums.PriorityEnum;
import com.rosy.common.enums.RepairStatusEnum;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.OrderNoGenerator;
import com.rosy.main.domain.dto.RepairOrderAssignDTO;
import com.rosy.main.domain.dto.RepairOrderCreateDTO;
import com.rosy.main.domain.entity.*;
import com.rosy.main.domain.vo.RepairEvaluationVO;
import com.rosy.main.domain.vo.RepairOrderVO;
import com.rosy.main.domain.vo.RepairRecordVO;
import com.rosy.main.mapper.EquipmentMapper;
import com.rosy.main.mapper.RepairEvaluationMapper;
import com.rosy.main.mapper.RepairOrderMapper;
import com.rosy.main.mapper.RepairRecordMapper;
import com.rosy.main.service.IRepairOrderService;
import com.rosy.main.service.INotificationService;
import com.rosy.main.service.IRepairmanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 报修工单Service实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RepairOrderServiceImpl extends ServiceImpl<RepairOrderMapper, RepairOrder> implements IRepairOrderService {

    private final RepairOrderMapper repairOrderMapper;
    private final EquipmentMapper equipmentMapper;
    private final RepairRecordMapper repairRecordMapper;
    private final RepairEvaluationMapper repairEvaluationMapper;
    private final IRepairmanService repairmanService;
    private final INotificationService notificationService;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RepairOrderVO createOrder(RepairOrderCreateDTO dto, Long userId, String userName, String userPhone) {
        Equipment equipment = equipmentMapper.selectById(dto.getEquipmentId());
        if (equipment == null) {
            throw new BusinessException("设备不存在");
        }

        RepairOrder order = new RepairOrder();
        order.setOrderNo(OrderNoGenerator.generate());
        order.setUserId(userId);
        order.setUserName(userName);
        order.setUserPhone(userPhone);
        order.setEquipmentId(equipment.getId());
        order.setEquipmentName(equipment.getEquipmentName());
        order.setEquipmentType(equipment.getEquipmentType());
        order.setLocation(equipment.getLocation());
        order.setFaultType(dto.getFaultType());
        order.setFaultDescription(dto.getFaultDescription());
        if (dto.getFaultImages() != null && !dto.getFaultImages().isEmpty()) {
            try {
                order.setFaultImages(objectMapper.writeValueAsString(dto.getFaultImages()));
            } catch (Exception e) {
                log.error("图片序列化失败", e);
            }
        }
        order.setPriority(dto.getPriority() != null ? dto.getPriority() : PriorityEnum.MEDIUM.getCode());
        order.setStatus(RepairStatusEnum.PENDING.getCode());
        order.setRemark(dto.getRemark());
        order.setCreator(userName);

        repairOrderMapper.insert(order);

        notificationService.createNotification(
                null, null,
                "新工单提醒",
                "设备" + equipment.getEquipmentName() + "收到报修申请",
                "order_created", order.getId(), "repair_order"
        );

        return convertToVO(order);
    }

    @Override
    public RepairOrderVO getOrderById(Long id) {
        RepairOrder order = repairOrderMapper.selectById(id);
        if (order == null) {
            throw new BusinessException("工单不存在");
        }
        return convertToVO(order);
    }

    @Override
    public Page<RepairOrderVO> pageOrder(Map<String, Object> params, long current, long size) {
        Page<RepairOrder> page = new Page<>(current, size);
        LambdaQueryWrapper<RepairOrder> wrapper = new LambdaQueryWrapper<>();

        if (params.get("status") != null) {
            wrapper.eq(RepairOrder::getStatus, params.get("status"));
        }
        if (params.get("priority") != null) {
            wrapper.eq(RepairOrder::getPriority, params.get("priority"));
        }
        if (params.get("userId") != null) {
            wrapper.eq(RepairOrder::getUserId, params.get("userId"));
        }
        if (params.get("repairmanId") != null) {
            wrapper.eq(RepairOrder::getRepairmanId, params.get("repairmanId"));
        }
        if (params.get("equipmentType") != null) {
            wrapper.eq(RepairOrder::getEquipmentType, params.get("equipmentType"));
        }
        if (params.get("orderNo") != null) {
            wrapper.like(RepairOrder::getOrderNo, params.get("orderNo"));
        }
        if (params.get("keyword") != null) {
            wrapper.and(w -> w.like(RepairOrder::getEquipmentName, params.get("keyword"))
                    .or().like(RepairOrder::getFaultDescription, params.get("keyword"))
                    .or().like(RepairOrder::getUserName, params.get("keyword")));
        }

        wrapper.orderByDesc(RepairOrder::getCreateTime);
        Page<RepairOrder> result = repairOrderMapper.selectPage(page, wrapper);

        Page<RepairOrderVO> voPage = new Page<>(current, size, result.getTotal());
        List<RepairOrderVO> voList = result.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        voPage.setRecords(voList);

        return voPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean assignOrder(RepairOrderAssignDTO dto, Long operatorId, String operatorName) {
        RepairOrder order = repairOrderMapper.selectById(dto.getOrderId());
        if (order == null) {
            throw new BusinessException("工单不存在");
        }

        if (!RepairStatusEnum.PENDING.getCode().equals(order.getStatus())) {
            throw new BusinessException("工单状态不允许分配");
        }

        Repairman repairman = repairmanService.getById(dto.getRepairmanId());
        if (repairman == null) {
            throw new BusinessException("维修人员不存在");
        }

        if (!"active".equals(repairman.getStatus())) {
            throw new BusinessException("维修人员当前不可用");
        }

        order.setStatus(RepairStatusEnum.ASSIGNED.getCode());
        order.setRepairmanId(repairman.getId());
        order.setRepairmanName(repairman.getName());
        order.setAssignmentTime(LocalDateTime.now());
        order.setUpdater(operatorName);
        repairOrderMapper.updateById(order);

        RepairRecord record = new RepairRecord();
        record.setOrderId(order.getId());
        record.setRecordType("assign");
        record.setContent("已分配给维修人员：" + repairman.getName() + "。备注：" + (dto.getRemark() != null ? dto.getRemark() : "无"));
        record.setOperatorId(operatorId);
        record.setOperatorName(operatorName);
        repairRecordMapper.insert(record);

        notificationService.createNotification(
                repairman.getUserId(), repairman.getName(),
                "工单分配通知",
                "您有新的维修工单需要处理：" + order.getOrderNo(),
                "order_assigned", order.getId(), "repair_order"
        );

        return true;
    }

    @Override
    public RepairOrderVO autoAssign(Long orderId, Long operatorId, String operatorName) {
        RepairOrder order = repairOrderMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException("工单不存在");
        }

        Repairman repairman = repairmanService.findAvailableRepairman(order.getEquipmentType());
        if (repairman == null) {
            throw new BusinessException("暂无可用的维修人员");
        }

        RepairOrderAssignDTO dto = new RepairOrderAssignDTO();
        dto.setOrderId(orderId);
        dto.setRepairmanId(repairman.getId());
        dto.setRemark("系统自动分配");

        assignOrder(dto, operatorId, operatorName);
        return getOrderById(orderId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean acceptOrder(Long orderId, Long repairmanId, String repairmanName) {
        RepairOrder order = repairOrderMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException("工单不存在");
        }

        if (!RepairStatusEnum.ASSIGNED.getCode().equals(order.getStatus())) {
            throw new BusinessException("工单状态不允许接单");
        }

        if (!repairmanId.equals(order.getRepairmanId())) {
            throw new BusinessException("您不是该工单的维修人员");
        }

        order.setStatus(RepairStatusEnum.PROCESSING.getCode());
        order.setAcceptTime(LocalDateTime.now());
        order.setStartTime(LocalDateTime.now());
        if (order.getAssignmentTime() != null) {
            order.setResponseTime((int) Duration.between(order.getAssignmentTime(), LocalDateTime.now()).toMinutes());
        }
        order.setUpdater(repairmanName);
        repairOrderMapper.updateById(order);

        RepairRecord record = new RepairRecord();
        record.setOrderId(order.getId());
        record.setRecordType("accept");
        record.setContent(repairmanName + "已确认接单，开始维修");
        record.setOperatorId(repairmanId);
        record.setOperatorName(repairmanName);
        repairRecordMapper.insert(record);

        notificationService.createNotification(
                order.getUserId(), order.getUserName(),
                "工单接单通知",
                "维修人员" + repairmanName + "已确认接单，正在处理您的报修申请",
                "order_accepted", order.getId(), "repair_order"
        );

        return true;
    }

    @Override
    public boolean startRepair(Long orderId, Long repairmanId) {
        RepairOrder order = repairOrderMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException("工单不存在");
        }

        if (!repairmanId.equals(order.getRepairmanId())) {
            throw new BusinessException("您不是该工单的维修人员");
        }

        if (!RepairStatusEnum.PROCESSING.getCode().equals(order.getStatus())) {
            order.setStatus(RepairStatusEnum.PROCESSING.getCode());
        }
        order.setStartTime(LocalDateTime.now());
        order.setUpdater(order.getRepairmanName());
        repairOrderMapper.updateById(order);

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean completeOrder(Long orderId, Long repairmanId, String remark, Double cost) {
        RepairOrder order = repairOrderMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException("工单不存在");
        }

        if (!repairmanId.equals(order.getRepairmanId())) {
            throw new BusinessException("您不是该工单的维修人员");
        }

        if (!RepairStatusEnum.PROCESSING.getCode().equals(order.getStatus())) {
            throw new BusinessException("工单状态不允许完成");
        }

        order.setStatus(RepairStatusEnum.COMPLETED.getCode());
        order.setEndTime(LocalDateTime.now());
        if (order.getStartTime() != null) {
            order.setRepairDuration((int) Duration.between(order.getStartTime(), LocalDateTime.now()).toMinutes());
        }
        if (cost != null) {
            order.setTotalCost(BigDecimal.valueOf(cost));
        }
        if (remark != null) {
            order.setRemark(remark);
        }
        order.setUpdater(order.getRepairmanName());
        repairOrderMapper.updateById(order);

        RepairRecord record = new RepairRecord();
        record.setOrderId(order.getId());
        record.setRecordType("complete");
        record.setContent("维修完成。" + (remark != null ? "备注：" + remark : ""));
        record.setOperatorId(repairmanId);
        record.setOperatorName(order.getRepairmanName());
        repairRecordMapper.insert(record);

        Repairman repairman = repairmanService.getById(repairmanId);
        if (repairman != null) {
            repairman.setCompletedOrders(repairman.getCompletedOrders() + 1);
            repairmanService.updateById(repairman);
        }

        notificationService.createNotification(
                order.getUserId(), order.getUserName(),
                "工单完成通知",
                "您的报修工单已完成，请进行评价",
                "order_completed", order.getId(), "repair_order"
        );

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelOrder(Long orderId, Long userId, String reason) {
        RepairOrder order = repairOrderMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException("工单不存在");
        }

        if (!userId.equals(order.getUserId())) {
            throw new BusinessException("只有创建者可以取消工单");
        }

        if (RepairStatusEnum.COMPLETED.getCode().equals(order.getStatus())) {
            throw new BusinessException("已完成的工单不允许取消");
        }

        order.setStatus(RepairStatusEnum.CANCELLED.getCode());
        order.setRemark((order.getRemark() != null ? order.getRemark() + "；" : "") + "取消原因：" + reason);
        repairOrderMapper.updateById(order);

        return true;
    }

    @Override
    public boolean updatePriority(Long orderId, String priority, Long operatorId) {
        RepairOrder order = repairOrderMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException("工单不存在");
        }

        order.setPriority(priority);
        order.setUpdater(operatorId.toString());
        repairOrderMapper.updateById(order);

        return true;
    }

    @Override
    public List<RepairOrderVO> getOrdersByUserId(Long userId, String status) {
        LambdaQueryWrapper<RepairOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RepairOrder::getUserId, userId);
        if (status != null) {
            wrapper.eq(RepairOrder::getStatus, status);
        }
        wrapper.orderByDesc(RepairOrder::getCreateTime);

        List<RepairOrder> orders = repairOrderMapper.selectList(wrapper);
        return orders.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    @Override
    public List<RepairOrderVO> getOrdersByRepairmanId(Long repairmanId, String status) {
        LambdaQueryWrapper<RepairOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RepairOrder::getRepairmanId, repairmanId);
        if (status != null) {
            wrapper.eq(RepairOrder::getStatus, status);
        }
        wrapper.orderByDesc(RepairOrder::getCreateTime);

        List<RepairOrder> orders = repairOrderMapper.selectList(wrapper);
        return orders.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    private RepairOrderVO convertToVO(RepairOrder order) {
        RepairOrderVO vo = new RepairOrderVO();
        vo.setId(order.getId());
        vo.setOrderNo(order.getOrderNo());
        vo.setUserId(order.getUserId());
        vo.setUserName(order.getUserName());
        vo.setUserPhone(order.getUserPhone());
        vo.setEquipmentId(order.getEquipmentId());
        vo.setEquipmentName(order.getEquipmentName());
        vo.setEquipmentType(order.getEquipmentType());
        vo.setLocation(order.getLocation());
        vo.setFaultType(order.getFaultType());
        vo.setFaultDescription(order.getFaultDescription());
        
        if (order.getFaultImages() != null) {
            try {
                vo.setFaultImages(objectMapper.readValue(order.getFaultImages(), new TypeReference<List<String>>(){}));
            } catch (Exception e) {
                log.error("图片反序列化失败", e);
            }
        }

        vo.setPriority(order.getPriority());
        PriorityEnum priorityEnum = PriorityEnum.getByCode(order.getPriority());
        vo.setPriorityDesc(priorityEnum != null ? priorityEnum.getDesc() : order.getPriority());

        vo.setStatus(order.getStatus());
        RepairStatusEnum statusEnum = RepairStatusEnum.getByCode(order.getStatus());
        vo.setStatusDesc(statusEnum != null ? statusEnum.getDesc() : order.getStatus());

        vo.setRepairmanId(order.getRepairmanId());
        vo.setRepairmanName(order.getRepairmanName());
        vo.setAssignmentTime(order.getAssignmentTime());
        vo.setAcceptTime(order.getAcceptTime());
        vo.setStartTime(order.getStartTime());
        vo.setEndTime(order.getEndTime());
        vo.setResponseTime(order.getResponseTime());
        vo.setRepairDuration(order.getRepairDuration());
        vo.setTotalCost(order.getTotalCost());
        vo.setRemark(order.getRemark());
        vo.setCreateTime(order.getCreateTime());
        vo.setUpdateTime(order.getUpdateTime());

        LambdaQueryWrapper<RepairRecord> recordWrapper = new LambdaQueryWrapper<>();
        recordWrapper.eq(RepairRecord::getOrderId, order.getId());
        recordWrapper.orderByAsc(RepairRecord::getCreateTime);
        List<RepairRecord> records = repairRecordMapper.selectList(recordWrapper);
        if (!records.isEmpty()) {
            List<RepairRecordVO> recordVOs = records.stream().map(r -> {
                RepairRecordVO recordVO = new RepairRecordVO();
                recordVO.setId(r.getId());
                recordVO.setOrderId(r.getOrderId());
                recordVO.setRecordType(r.getRecordType());
                recordVO.setRecordTypeDesc(getRecordTypeDesc(r.getRecordType()));
                recordVO.setContent(r.getContent());
                if (r.getImages() != null) {
                    try {
                        recordVO.setImages(objectMapper.readValue(r.getImages(), new TypeReference<List<String>>(){}));
                    } catch (Exception e) {
                        log.error("图片反序列化失败", e);
                    }
                }
                recordVO.setOperatorId(r.getOperatorId());
                recordVO.setOperatorName(r.getOperatorName());
                recordVO.setCreateTime(r.getCreateTime());
                return recordVO;
            }).collect(Collectors.toList());
            vo.setRecords(recordVOs);
        }

        LambdaQueryWrapper<RepairEvaluation> evalWrapper = new LambdaQueryWrapper<>();
        evalWrapper.eq(RepairEvaluation::getOrderId, order.getId());
        RepairEvaluation evaluation = repairEvaluationMapper.selectOne(evalWrapper);
        if (evaluation != null) {
            RepairEvaluationVO evalVO = new RepairEvaluationVO();
            evalVO.setId(evaluation.getId());
            evalVO.setOrderId(evaluation.getOrderId());
            evalVO.setUserId(evaluation.getUserId());
            evalVO.setUserName(evaluation.getUserName());
            evalVO.setRepairmanId(evaluation.getRepairmanId());
            evalVO.setRepairmanName(evaluation.getRepairmanName());
            evalVO.setRating(evaluation.getRating());
            evalVO.setContent(evaluation.getContent());
            if (evaluation.getImages() != null) {
                try {
                    evalVO.setImages(objectMapper.readValue(evaluation.getImages(), new TypeReference<List<String>>(){}));
                } catch (Exception e) {
                    log.error("图片反序列化失败", e);
                }
            }
            evalVO.setResponseRating(evaluation.getResponseRating());
            evalVO.setServiceRating(evaluation.getServiceRating());
            evalVO.setQualityRating(evaluation.getQualityRating());
            evalVO.setCreateTime(evaluation.getCreateTime());
            vo.setEvaluation(evalVO);
        }

        return vo;
    }

    private String getRecordTypeDesc(String type) {
        return switch (type) {
            case "assign" -> "工单分配";
            case "accept" -> "确认接单";
            case "process" -> "维修记录";
            case "complete" -> "维修完成";
            default -> type;
        };
    }
}
