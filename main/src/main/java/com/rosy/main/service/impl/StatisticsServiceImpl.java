package com.rosy.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rosy.main.domain.entity.RepairOrder;
import com.rosy.main.domain.vo.repair.StatisticsVO;
import com.rosy.main.enums.PriorityEnum;
import com.rosy.main.enums.RepairOrderStatusEnum;
import com.rosy.main.service.IRepairOrderService;
import com.rosy.main.service.IStatisticsService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatisticsServiceImpl implements IStatisticsService {

    @Resource
    private IRepairOrderService repairOrderService;

    @Override
    public StatisticsVO getStatistics(LocalDateTime startTime, LocalDateTime endTime) {
        StatisticsVO vo = new StatisticsVO();

        LambdaQueryWrapper<RepairOrder> baseWrapper = new LambdaQueryWrapper<>();
        if (startTime != null) {
            baseWrapper.ge(RepairOrder::getCreateTime, startTime);
        }
        if (endTime != null) {
            baseWrapper.le(RepairOrder::getCreateTime, endTime);
        }

        List<RepairOrder> allOrders = repairOrderService.list(baseWrapper);

        vo.setTotalOrders((long) allOrders.size());
        vo.setPendingOrders(allOrders.stream().filter(o -> RepairOrderStatusEnum.PENDING.getCode().equals(o.getStatus().intValue())).count());
        vo.setProcessingOrders(allOrders.stream().filter(o -> 
                RepairOrderStatusEnum.ASSIGNED.getCode().equals(o.getStatus().intValue()) || 
                RepairOrderStatusEnum.REPAIRING.getCode().equals(o.getStatus().intValue())).count());
        vo.setCompletedOrders(allOrders.stream().filter(o -> RepairOrderStatusEnum.COMPLETED.getCode().equals(o.getStatus().intValue())).count());

        List<RepairOrder> completedOrders = allOrders.stream()
                .filter(o -> RepairOrderStatusEnum.COMPLETED.getCode().equals(o.getStatus().intValue()))
                .filter(o -> o.getAssignTime() != null && o.getCompleteTime() != null)
                .toList();

        if (!completedOrders.isEmpty()) {
            long totalResponseTime = completedOrders.stream()
                    .mapToLong(o -> java.time.Duration.between(o.getAssignTime(), o.getAcceptTime() != null ? o.getAcceptTime() : o.getCompleteTime()).toMinutes())
                    .sum();
            vo.setAvgResponseTime(BigDecimal.valueOf(totalResponseTime)
                    .divide(BigDecimal.valueOf(completedOrders.size()), 2, RoundingMode.HALF_UP));

            long totalRepairTime = completedOrders.stream()
                    .mapToLong(o -> java.time.Duration.between(o.getAcceptTime() != null ? o.getAcceptTime() : o.getAssignTime(), o.getCompleteTime()).toMinutes())
                    .sum();
            vo.setAvgRepairTime(BigDecimal.valueOf(totalRepairTime)
                    .divide(BigDecimal.valueOf(completedOrders.size()), 2, RoundingMode.HALF_UP));
        }

        Map<String, Long> faultTypeCount = allOrders.stream()
                .collect(Collectors.groupingBy(RepairOrder::getFaultType, Collectors.counting()));
        List<StatisticsVO.FaultTypeStatistics> faultTypeStatistics = faultTypeCount.entrySet().stream()
                .map(entry -> {
                    StatisticsVO.FaultTypeStatistics stat = new StatisticsVO.FaultTypeStatistics();
                    stat.setFaultType(entry.getKey());
                    stat.setCount(entry.getValue());
                    stat.setPercentage(BigDecimal.valueOf(entry.getValue())
                            .multiply(BigDecimal.valueOf(100))
                            .divide(BigDecimal.valueOf(allOrders.size()), 2, RoundingMode.HALF_UP));
                    return stat;
                })
                .toList();
        vo.setFaultTypeStatistics(faultTypeStatistics);

        Map<String, Long> deviceTypeCount = allOrders.stream()
                .collect(Collectors.groupingBy(RepairOrder::getDeviceType, Collectors.counting()));
        List<StatisticsVO.DeviceTypeStatistics> deviceTypeStatistics = deviceTypeCount.entrySet().stream()
                .map(entry -> {
                    StatisticsVO.DeviceTypeStatistics stat = new StatisticsVO.DeviceTypeStatistics();
                    stat.setDeviceType(entry.getKey());
                    stat.setCount(entry.getValue());
                    stat.setPercentage(BigDecimal.valueOf(entry.getValue())
                            .multiply(BigDecimal.valueOf(100))
                            .divide(BigDecimal.valueOf(allOrders.size()), 2, RoundingMode.HALF_UP));
                    return stat;
                })
                .toList();
        vo.setDeviceTypeStatistics(deviceTypeStatistics);

        Map<Integer, Long> priorityCount = allOrders.stream()
                .collect(Collectors.groupingBy(o -> o.getPriority().intValue(), Collectors.counting()));
        List<StatisticsVO.PriorityStatistics> priorityStatistics = priorityCount.entrySet().stream()
                .map(entry -> {
                    StatisticsVO.PriorityStatistics stat = new StatisticsVO.PriorityStatistics();
                    stat.setPriority(entry.getKey());
                    PriorityEnum priorityEnum = PriorityEnum.getByCode(entry.getKey());
                    stat.setPriorityDesc(priorityEnum != null ? priorityEnum.getDesc() : "");
                    stat.setCount(entry.getValue());
                    return stat;
                })
                .toList();
        vo.setPriorityStatistics(priorityStatistics);

        Map<String, Long> monthlyCount = allOrders.stream()
                .collect(Collectors.groupingBy(o -> o.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM")), Collectors.counting()));
        List<StatisticsVO.MonthlyStatistics> monthlyStatistics = monthlyCount.entrySet().stream()
                .map(entry -> {
                    StatisticsVO.MonthlyStatistics stat = new StatisticsVO.MonthlyStatistics();
                    stat.setMonth(entry.getKey());
                    stat.setCount(entry.getValue());
                    return stat;
                })
                .sorted((a, b) -> b.getMonth().compareTo(a.getMonth()))
                .toList();
        vo.setMonthlyStatistics(monthlyStatistics);

        return vo;
    }
}
