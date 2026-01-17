package com.rosy.repair.service.impl;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.repair.domain.entity.RepairNotification;
import com.rosy.repair.domain.entity.RepairOrder;
import com.rosy.repair.domain.entity.Repairer;
import com.rosy.repair.domain.request.*;
import com.rosy.repair.domain.vo.RepairOrderVO;
import com.rosy.repair.domain.vo.StatisticsVO;
import com.rosy.repair.mapper.RepairNotificationMapper;
import com.rosy.repair.mapper.RepairOrderMapper;
import com.rosy.repair.mapper.RepairerMapper;
import com.rosy.repair.service.IRepairOrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RepairOrderServiceImpl extends ServiceImpl<RepairOrderMapper, RepairOrder> implements IRepairOrderService {

    private final RepairerMapper repairerMapper;
    private final RepairNotificationMapper notificationMapper;

    public RepairOrderServiceImpl(RepairerMapper repairerMapper, RepairNotificationMapper notificationMapper) {
        this.repairerMapper = repairerMapper;
        this.notificationMapper = notificationMapper;
    }

    @Override
    @Transactional
    public RepairOrderVO createOrder(RepairOrderCreateRequest request, Long userId) {
        RepairOrder order = new RepairOrder();
        order.setOrderNo("RO" + System.currentTimeMillis() + IdUtil.fastSimpleUUID().substring(0, 4).toUpperCase());
        order.setUserId(userId);
        order.setDeviceType(request.getDeviceType());
        order.setLocation(request.getLocation());
        order.setFaultType(request.getFaultType());
        order.setDescription(request.getDescription());
        order.setImages(request.getImages() != null ? JSON.toJSONString(request.getImages()) : null);
        order.setStatus(0);
        order.setPriority(0);
        order.setAssignType(0);

        this.save(order);

        sendNotification(userId, order.getId(), 0, "您的报修工单已创建，单号：" + order.getOrderNo());

        return convertToVO(order);
    }

    @Override
    @Transactional
    public void assignOrder(RepairOrderAssignRequest request) {
        RepairOrder order = this.getById(request.getOrderId());
        if (order == null || order.getStatus() != 0) {
            throw new RuntimeException("工单不存在或状态不允许分配");
        }

        order.setRepairerId(request.getRepairerId());
        order.setAssignType(1);
        order.setAssignTime(LocalDateTime.now());
        if (request.getPriority() != null) {
            order.setPriority(request.getPriority());
        }
        this.updateById(order);

        sendNotification(order.getUserId(), order.getId(), 1, "您的工单已分配给维修人员，请等待接单");
        sendNotification(request.getRepairerId(), order.getId(), 1, "有新工单分配给您，请及时处理");
    }

    @Override
    @Transactional
    public void autoAssignOrder(Long orderId) {
        RepairOrder order = this.getById(orderId);
        if (order == null || order.getStatus() != 0) {
            throw new RuntimeException("工单不存在或状态不允许分配");
        }

        List<Repairer> repairers = repairerMapper.selectList(
                new LambdaQueryWrapper<Repairer>()
                        .eq(Repairer::getStatus, 0)
                        .orderByAsc(Repairer::getOrderCount)
        );

        if (repairers.isEmpty()) {
            throw new RuntimeException("暂无可用维修人员");
        }

        Repairer repairer = repairers.get(0);
        order.setRepairerId(repairer.getId());
        order.setAssignType(0);
        order.setAssignTime(LocalDateTime.now());
        this.updateById(order);

        sendNotification(order.getUserId(), order.getId(), 1, "您的工单已自动分配给维修人员，请等待接单");
        sendNotification(repairer.getId(), order.getId(), 1, "有新工单自动分配给您，请及时处理");
    }

    @Override
    @Transactional
    public void acceptOrder(Long orderId, Long repairerId) {
        RepairOrder order = this.getById(orderId);
        if (order == null || !order.getRepairerId().equals(repairerId) || order.getStatus() != 0) {
            throw new RuntimeException("无法接单，工单状态不正确");
        }

        order.setStatus(1);
        order.setAcceptTime(LocalDateTime.now());
        this.updateById(order);

        Repairer repairer = repairerMapper.selectById(repairerId);
        repairer.setStatus(1);
        repairerMapper.updateById(repairer);

        sendNotification(order.getUserId(), order.getId(), 2, "维修人员已接单，正在处理中");
    }

    @Override
    @Transactional
    public void completeOrder(RepairRecordRequest request, Long repairerId) {
        RepairOrder order = this.getById(request.getOrderId());
        if (order == null || !order.getRepairerId().equals(repairerId) || order.getStatus() != 1) {
            throw new RuntimeException("无法完成，工单状态不正确");
        }

        order.setStatus(2);
        order.setCompleteTime(LocalDateTime.now());
        order.setRepairRecord(request.getRepairRecord());
        order.setResult(request.getResult());
        this.updateById(order);

        Repairer repairer = repairerMapper.selectById(repairerId);
        repairer.setStatus(0);
        repairer.setOrderCount(repairer.getOrderCount() + 1);
        repairerMapper.updateById(repairer);

        sendNotification(order.getUserId(), order.getId(), 3, "您的工单已维修完成，请进行评价");
    }

    @Override
    @Transactional
    public void evaluateOrder(EvaluationRequest request, Long userId) {
        RepairOrder order = this.getById(request.getOrderId());
        if (order == null || !order.getUserId().equals(userId) || order.getStatus() != 2) {
            throw new RuntimeException("无法评价，工单状态不正确");
        }
        if (order.getStar() != null) {
            throw new RuntimeException("该工单已评价过");
        }

        order.setStar(request.getStar());
        order.setEvaluation(request.getEvaluation());
        this.updateById(order);

        sendNotification(order.getRepairerId(), order.getId(), 4, "您完成的工单收到了评价");
    }

    @Override
    public RepairOrderVO getOrderDetail(Long orderId) {
        RepairOrder order = this.getById(orderId);
        if (order == null) {
            return null;
        }
        return convertToVO(order);
    }

    @Override
    public List<RepairOrderVO> getOrderList(Integer status, Long userId, Long repairerId) {
        LambdaQueryWrapper<RepairOrder> wrapper = new LambdaQueryWrapper<>();
        if (status != null) {
            wrapper.eq(RepairOrder::getStatus, status);
        }
        if (userId != null) {
            wrapper.eq(RepairOrder::getUserId, userId);
        }
        if (repairerId != null) {
            wrapper.eq(RepairOrder::getRepairerId, repairerId);
        }
        wrapper.orderByDesc(RepairOrder::getCreateTime);

        List<RepairOrder> orders = this.list(wrapper);
        return orders.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    @Override
    public StatisticsVO getStatistics(LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime == null) {
            startTime = LocalDateTime.now().minusMonths(1);
        }
        if (endTime == null) {
            endTime = LocalDateTime.now();
        }

        StatisticsVO vo = new StatisticsVO();
        vo.setTotalOrders(this.count(new LambdaQueryWrapper<RepairOrder>()
                .ge(RepairOrder::getCreateTime, startTime)
                .le(RepairOrder::getCreateTime, endTime)));
        vo.setPendingOrders(this.count(new LambdaQueryWrapper<RepairOrder>()
                .eq(RepairOrder::getStatus, 0)
                .ge(RepairOrder::getCreateTime, startTime)
                .le(RepairOrder::getCreateTime, endTime)));
        vo.setProcessingOrders(this.count(new LambdaQueryWrapper<RepairOrder>()
                .eq(RepairOrder::getStatus, 1)
                .ge(RepairOrder::getCreateTime, startTime)
                .le(RepairOrder::getCreateTime, endTime)));
        vo.setCompletedOrders(this.count(new LambdaQueryWrapper<RepairOrder>()
                .eq(RepairOrder::getStatus, 2)
                .ge(RepairOrder::getCreateTime, startTime)
                .le(RepairOrder::getCreateTime, endTime)));

        Long avgResponseTime = baseMapper.selectAvgResponseTime(startTime, endTime);
        if (avgResponseTime != null) {
            vo.setAvgResponseTime(BigDecimal.valueOf(avgResponseTime).divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP));
        }

        vo.setFaultTypeAnalysis(baseMapper.selectFaultTypeAnalysis(startTime, endTime));
        vo.setDeviceTypeAnalysis(baseMapper.selectDeviceTypeAnalysis(startTime, endTime));
        vo.setPriorityAnalysis(baseMapper.selectPriorityAnalysis(startTime, endTime));

        return vo;
    }

    private RepairOrderVO convertToVO(RepairOrder order) {
        RepairOrderVO vo = new RepairOrderVO();
        BeanUtils.copyProperties(order, vo);
        if (order.getImages() != null) {
            vo.setImages(JSON.parseArray(order.getImages(), String.class));
        }
        if (order.getRepairerId() != null) {
            Repairer repairer = repairerMapper.selectById(order.getRepairerId());
            if (repairer != null) {
                vo.setRepairerName(repairer.getName());
            }
        }
        return vo;
    }

    private void sendNotification(Long userId, Long orderId, Integer type, String content) {
        RepairNotification notification = new RepairNotification();
        notification.setUserId(userId);
        notification.setOrderId(orderId);
        notification.setType(type);
        notification.setContent(content);
        notification.setIsRead(0);
        notificationMapper.insert(notification);
    }
}