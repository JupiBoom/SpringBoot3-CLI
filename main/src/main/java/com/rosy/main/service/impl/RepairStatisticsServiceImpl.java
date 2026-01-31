package com.rosy.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rosy.main.domain.entity.DeviceType;
import com.rosy.main.domain.entity.Evaluation;
import com.rosy.main.domain.entity.FaultType;
import com.rosy.main.domain.entity.RepairOrder;
import com.rosy.main.enums.RepairStatusEnum;
import com.rosy.main.mapper.DeviceTypeMapper;
import com.rosy.main.mapper.EvaluationMapper;
import com.rosy.main.mapper.FaultTypeMapper;
import com.rosy.main.mapper.RepairOrderMapper;
import com.rosy.main.service.IRepairStatisticsService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 报修统计服务实现
 *
 * @author Rosy
 * @since 2025-01-31
 */
@Service
public class RepairStatisticsServiceImpl implements IRepairStatisticsService {

    @Resource
    private RepairOrderMapper repairOrderMapper;

    @Resource
    private EvaluationMapper evaluationMapper;

    @Resource
    private DeviceTypeMapper deviceTypeMapper;

    @Resource
    private FaultTypeMapper faultTypeMapper;

    @Override
    public Map<String, Object> getResponseTimeStatistics(LocalDateTime startDate, LocalDateTime endDate) {
        Map<String, Object> result = new HashMap<>();
        
        // 查询指定时间范围内的工单
        QueryWrapper<RepairOrder> queryWrapper = new QueryWrapper<>();
        if (startDate != null) {
            queryWrapper.ge("create_time", startDate);
        }
        if (endDate != null) {
            queryWrapper.le("create_time", endDate);
        }
        queryWrapper.isNotNull("accepted_time");
        queryWrapper.isNotNull("completed_time");
        queryWrapper.eq("status", RepairStatusEnum.COMPLETED.getCode());
        
        List<RepairOrder> completedOrders = repairOrderMapper.selectList(queryWrapper);
        
        if (completedOrders.isEmpty()) {
            result.put("averageResponseTime", 0);
            result.put("averageRepairTime", 0);
            result.put("averageTotalTime", 0);
            return result;
        }
        
        // 计算响应时间（创建时间到接单时间）
        long totalResponseTime = 0;
        long totalRepairTime = 0;
        long totalTotalTime = 0;
        int responseTimeCount = 0;
        int repairTimeCount = 0;
        int totalTimeCount = 0;
        
        for (RepairOrder order : completedOrders) {
            // 响应时间（分钟）
            if (order.getCreateTime() != null && order.getAcceptedTime() != null) {
                long responseMinutes = java.time.Duration.between(order.getCreateTime(), order.getAcceptedTime()).toMinutes();
                totalResponseTime += responseMinutes;
                responseTimeCount++;
            }
            
            // 维修时间（分钟）
            if (order.getAcceptedTime() != null && order.getCompletedTime() != null) {
                long repairMinutes = java.time.Duration.between(order.getAcceptedTime(), order.getCompletedTime()).toMinutes();
                totalRepairTime += repairMinutes;
                repairTimeCount++;
            }
            
            // 总时间（分钟）
            if (order.getCreateTime() != null && order.getCompletedTime() != null) {
                long totalMinutes = java.time.Duration.between(order.getCreateTime(), order.getCompletedTime()).toMinutes();
                totalTotalTime += totalMinutes;
                totalTimeCount++;
            }
        }
        
        result.put("averageResponseTime", responseTimeCount > 0 ? totalResponseTime / responseTimeCount : 0);
        result.put("averageRepairTime", repairTimeCount > 0 ? totalRepairTime / repairTimeCount : 0);
        result.put("averageTotalTime", totalTimeCount > 0 ? totalTotalTime / totalTimeCount : 0);
        
        return result;
    }

    @Override
    public List<Map<String, Object>> getFaultTypeAnalysis(LocalDateTime startDate, LocalDateTime endDate) {
        List<Map<String, Object>> result = new ArrayList<>();
        
        // 查询指定时间范围内的工单
        QueryWrapper<RepairOrder> queryWrapper = new QueryWrapper<>();
        if (startDate != null) {
            queryWrapper.ge("create_time", startDate);
        }
        if (endDate != null) {
            queryWrapper.le("create_time", endDate);
        }
        queryWrapper.isNotNull("fault_type_id");
        
        List<RepairOrder> orders = repairOrderMapper.selectList(queryWrapper);
        
        // 统计各故障类型的数量
        Map<Long, Integer> faultTypeCountMap = new HashMap<>();
        for (RepairOrder order : orders) {
            Long faultTypeId = order.getFaultTypeId();
            faultTypeCountMap.put(faultTypeId, faultTypeCountMap.getOrDefault(faultTypeId, 0) + 1);
        }
        
        // 获取故障类型名称并构建结果
        for (Map.Entry<Long, Integer> entry : faultTypeCountMap.entrySet()) {
            Long faultTypeId = entry.getKey();
            Integer count = entry.getValue();
            
            FaultType faultType = faultTypeMapper.selectById(faultTypeId);
            if (faultType != null) {
                Map<String, Object> item = new HashMap<>();
                item.put("faultTypeId", faultTypeId);
                item.put("faultTypeName", faultType.getName());
                item.put("count", count);
                result.add(item);
            }
        }
        
        // 按数量降序排序
        result.sort((a, b) -> ((Integer) b.get("count")).compareTo((Integer) a.get("count")));
        
        return result;
    }

    @Override
    public Map<String, Object> getOrderStatistics(LocalDateTime startDate, LocalDateTime endDate) {
        Map<String, Object> result = new HashMap<>();
        
        // 查询指定时间范围内的工单
        QueryWrapper<RepairOrder> queryWrapper = new QueryWrapper<>();
        if (startDate != null) {
            queryWrapper.ge("create_time", startDate);
        }
        if (endDate != null) {
            queryWrapper.le("create_time", endDate);
        }
        
        List<RepairOrder> orders = repairOrderMapper.selectList(queryWrapper);
        
        // 统计各状态的工单数量
        Map<Byte, Integer> statusCountMap = new HashMap<>();
        for (RepairOrder order : orders) {
            Byte status = order.getStatus();
            statusCountMap.put(status, statusCountMap.getOrDefault(status, 0) + 1);
        }
        
        // 构建结果
        Map<String, Integer> statusCount = new HashMap<>();
        statusCount.put("pending", statusCountMap.getOrDefault((byte) RepairStatusEnum.PENDING.getCode(), 0));
        statusCount.put("inProgress", statusCountMap.getOrDefault((byte) RepairStatusEnum.IN_PROGRESS.getCode(), 0));
        statusCount.put("completed", statusCountMap.getOrDefault((byte) RepairStatusEnum.COMPLETED.getCode(), 0));
        
        result.put("statusCount", statusCount);
        result.put("totalCount", orders.size());
        
        // 统计各优先级的工单数量
        Map<Byte, Integer> priorityCountMap = new HashMap<>();
        for (RepairOrder order : orders) {
            Byte priority = order.getPriority();
            priorityCountMap.put(priority, priorityCountMap.getOrDefault(priority, 0) + 1);
        }
        
        Map<String, Integer> priorityCount = new HashMap<>();
        priorityCount.put("low", priorityCountMap.getOrDefault((byte) 1, 0));
        priorityCount.put("medium", priorityCountMap.getOrDefault((byte) 2, 0));
        priorityCount.put("high", priorityCountMap.getOrDefault((byte) 3, 0));
        priorityCount.put("urgent", priorityCountMap.getOrDefault((byte) 4, 0));
        
        result.put("priorityCount", priorityCount);
        
        return result;
    }

    @Override
    public List<Map<String, Object>> getTechnicianWorkloadStatistics(LocalDateTime startDate, LocalDateTime endDate) {
        List<Map<String, Object>> result = new ArrayList<>();
        
        // 查询指定时间范围内已完成的工单
        QueryWrapper<RepairOrder> queryWrapper = new QueryWrapper<>();
        if (startDate != null) {
            queryWrapper.ge("create_time", startDate);
        }
        if (endDate != null) {
            queryWrapper.le("create_time", endDate);
        }
        queryWrapper.isNotNull("assigned_to");
        queryWrapper.eq("status", RepairStatusEnum.COMPLETED.getCode());
        
        List<RepairOrder> orders = repairOrderMapper.selectList(queryWrapper);
        
        // 统计各维修人员的工作量
        Map<Long, Integer> technicianCountMap = new HashMap<>();
        Map<Long, Long> technicianTotalTimeMap = new HashMap<>();
        
        for (RepairOrder order : orders) {
            Long technicianId = order.getAssignedTo();
            technicianCountMap.put(technicianId, technicianCountMap.getOrDefault(technicianId, 0) + 1);
            
            // 计算维修时间
            if (order.getAcceptedTime() != null && order.getCompletedTime() != null) {
                long repairMinutes = java.time.Duration.between(order.getAcceptedTime(), order.getCompletedTime()).toMinutes();
                technicianTotalTimeMap.put(technicianId, technicianTotalTimeMap.getOrDefault(technicianId, 0L) + repairMinutes);
            }
        }
        
        // 构建结果
        for (Map.Entry<Long, Integer> entry : technicianCountMap.entrySet()) {
            Long technicianId = entry.getKey();
            Integer count = entry.getValue();
            
            Map<String, Object> item = new HashMap<>();
            item.put("technicianId", technicianId);
            item.put("orderCount", count);
            item.put("totalRepairTime", technicianTotalTimeMap.getOrDefault(technicianId, 0L));
            item.put("averageRepairTime", count > 0 ? technicianTotalTimeMap.getOrDefault(technicianId, 0L) / count : 0);
            result.add(item);
        }
        
        // 按工单数量降序排序
        result.sort((a, b) -> ((Integer) b.get("orderCount")).compareTo((Integer) a.get("orderCount")));
        
        return result;
    }

    @Override
    public Map<String, Object> getEvaluationStatistics(LocalDateTime startDate, LocalDateTime endDate) {
        Map<String, Object> result = new HashMap<>();
        
        // 查询指定时间范围内的评价
        QueryWrapper<Evaluation> queryWrapper = new QueryWrapper<>();
        if (startDate != null) {
            queryWrapper.ge("create_time", startDate);
        }
        if (endDate != null) {
            queryWrapper.le("create_time", endDate);
        }
        
        List<Evaluation> evaluations = evaluationMapper.selectList(queryWrapper);
        
        if (evaluations.isEmpty()) {
            result.put("totalEvaluations", 0);
            result.put("averageRating", 0);
            result.put("ratingDistribution", new HashMap<>());
            return result;
        }
        
        // 统计各评分的数量
        Map<Byte, Integer> ratingCountMap = new HashMap<>();
        long totalRating = 0;
        
        for (Evaluation evaluation : evaluations) {
            Byte rating = evaluation.getRating();
            ratingCountMap.put(rating, ratingCountMap.getOrDefault(rating, 0) + 1);
            totalRating += rating;
        }
        
        // 构建评分分布
        Map<String, Integer> ratingDistribution = new HashMap<>();
        for (int i = 1; i <= 5; i++) {
            ratingDistribution.put(String.valueOf(i), ratingCountMap.getOrDefault((byte) i, 0));
        }
        
        result.put("totalEvaluations", evaluations.size());
        result.put("averageRating", totalRating / (double) evaluations.size());
        result.put("ratingDistribution", ratingDistribution);
        
        return result;
    }

    @Override
    public List<Map<String, Object>> getMonthlyOrderTrend(Integer months) {
        List<Map<String, Object>> result = new ArrayList<>();
        
        // 默认查询最近12个月
        if (months == null || months <= 0) {
            months = 12;
        }
        
        // 计算开始日期
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusMonths(months);
        
        // 按月分组统计工单数量
        QueryWrapper<RepairOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("create_time", startDate);
        queryWrapper.le("create_time", endDate);
        queryWrapper.select("DATE_FORMAT(create_time, '%Y-%m') as month", "COUNT(*) as count");
        queryWrapper.groupBy("DATE_FORMAT(create_time, '%Y-%m')");
        queryWrapper.orderByAsc("month");
        
        List<Map<String, Object>> monthlyData = repairOrderMapper.selectMaps(queryWrapper);
        
        // 补充缺失的月份
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        for (int i = 0; i < months; i++) {
            LocalDateTime monthDate = startDate.plusMonths(i);
            String monthStr = monthDate.format(formatter);
            
            Map<String, Object> monthData = new HashMap<>();
            monthData.put("month", monthStr);
            
            // 查找该月的数据
            boolean found = false;
            for (Map<String, Object> data : monthlyData) {
                if (monthStr.equals(data.get("month"))) {
                    monthData.put("count", data.get("count"));
                    found = true;
                    break;
                }
            }
            
            if (!found) {
                monthData.put("count", 0);
            }
            
            result.add(monthData);
        }
        
        return result;
    }

    @Override
    public List<Map<String, Object>> getDeviceTypeFailureRate(LocalDateTime startDate, LocalDateTime endDate) {
        List<Map<String, Object>> result = new ArrayList<>();
        
        // 查询所有设备类型
        List<DeviceType> deviceTypes = deviceTypeMapper.selectList(null);
        
        for (DeviceType deviceType : deviceTypes) {
            Map<String, Object> item = new HashMap<>();
            item.put("deviceTypeId", deviceType.getId());
            item.put("deviceTypeName", deviceType.getName());
            
            // 查询该设备类型的工单数量
            QueryWrapper<RepairOrder> queryWrapper = new QueryWrapper<>();
            if (startDate != null) {
                queryWrapper.ge("create_time", startDate);
            }
            if (endDate != null) {
                queryWrapper.le("create_time", endDate);
            }
            queryWrapper.eq("device_type_id", deviceType.getId());
            
            int orderCount = Math.toIntExact(repairOrderMapper.selectCount(queryWrapper));
            item.put("orderCount", orderCount);
            
            // 这里可以添加设备总数来计算故障率，暂时使用工单数量作为故障率指标
            item.put("failureRate", orderCount);
            
            result.add(item);
        }
        
        // 按工单数量降序排序
        result.sort((a, b) -> ((Integer) b.get("orderCount")).compareTo((Integer) a.get("orderCount")));
        
        return result;
    }
}