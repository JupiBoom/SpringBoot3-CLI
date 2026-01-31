package com.rosy.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rosy.common.enums.RepairOrderStatusEnum;
import com.rosy.main.domain.entity.RepairOrder;
import com.rosy.main.domain.entity.RepairStaff;
import com.rosy.main.domain.vo.RepairStatisticsVO;
import com.rosy.main.mapper.RepairEvaluationMapper;
import com.rosy.main.mapper.RepairOrderMapper;
import com.rosy.main.mapper.RepairStaffMapper;
import com.rosy.main.service.IRepairStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class RepairStatisticsServiceImpl implements IRepairStatisticsService {

    private final RepairOrderMapper repairOrderMapper;
    private final RepairEvaluationMapper repairEvaluationMapper;
    private final RepairStaffMapper repairStaffMapper;

    @Override
    public RepairStatisticsVO getOverallStatistics() {
        RepairStatisticsVO vo = new RepairStatisticsVO();
        
        Long totalOrders = repairOrderMapper.selectCount(null);
        vo.setTotalOrders(totalOrders);
        
        LambdaQueryWrapper<RepairOrder> pendingWrapper = new LambdaQueryWrapper<>();
        pendingWrapper.eq(RepairOrder::getStatus, RepairOrderStatusEnum.PENDING.getCode());
        vo.setPendingOrders(repairOrderMapper.selectCount(pendingWrapper));
        
        LambdaQueryWrapper<RepairOrder> processingWrapper = new LambdaQueryWrapper<>();
        processingWrapper.in(RepairOrder::getStatus, 
            RepairOrderStatusEnum.ASSIGNED.getCode(), 
            RepairOrderStatusEnum.REPAIRING.getCode());
        vo.setProcessingOrders(repairOrderMapper.selectCount(processingWrapper));
        
        LambdaQueryWrapper<RepairOrder> completedWrapper = new LambdaQueryWrapper<>();
        completedWrapper.eq(RepairOrder::getStatus, RepairOrderStatusEnum.COMPLETED.getCode());
        vo.setCompletedOrders(repairOrderMapper.selectCount(completedWrapper));
        
        Double avgResponse = repairOrderMapper.getAvgResponseMinutes();
        vo.setAvgResponseMinutes(avgResponse != null ? 
            BigDecimal.valueOf(avgResponse).setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO);
        
        List<Integer> durations = repairOrderMapper.getRepairDurationMinutes();
        if (!durations.isEmpty()) {
            double avg = durations.stream().mapToInt(Integer::intValue).average().orElse(0);
            vo.setAvgRepairMinutes(BigDecimal.valueOf(avg).setScale(2, RoundingMode.HALF_UP));
        } else {
            vo.setAvgRepairMinutes(BigDecimal.ZERO);
        }
        
        Double avgRating = repairEvaluationMapper.getOverallAvgRating();
        vo.setAvgRating(avgRating != null ? 
            BigDecimal.valueOf(avgRating).setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO);
        
        vo.setFaultTypeStatistics(getFaultTypeStats());
        vo.setOrderStatusStatistics(getOrderStatusStats());
        vo.setMonthlyOrderTrend(getMonthlyTrend());
        vo.setStaffRanking(getStaffRankingList());
        
        return vo;
    }

    private List<Map<String, Object>> getFaultTypeStats() {
        return repairOrderMapper.getFaultTypeStatistics();
    }

    private List<Map<String, Object>> getOrderStatusStats() {
        List<Map<String, Object>> stats = repairOrderMapper.getOrderStatusStatistics();
        for (Map<String, Object> map : stats) {
            Object status = map.get("status");
            if (status != null) {
                map.put("statusDesc", RepairOrderStatusEnum.getDescByCode(((Number) status).intValue()));
            }
        }
        return stats;
    }

    private List<Map<String, Object>> getMonthlyTrend() {
        LocalDateTime endTime = LocalDateTime.now();
        LocalDateTime startTime = endTime.minusMonths(6);
        return repairOrderMapper.getOrderTrend(startTime, endTime);
    }

    private List<Map<String, Object>> getStaffRankingList() {
        List<RepairStaff> staffList = repairStaffMapper.selectStaffRanking();
        List<Map<String, Object>> ranking = new ArrayList<>();
        for (RepairStaff staff : staffList) {
            Map<String, Object> map = new HashMap<>();
            map.put("staffId", staff.getId());
            map.put("staffName", staff.getStaffName());
            map.put("orderCount", staff.getOrderCount());
            Double avgRating = repairEvaluationMapper.getAvgRatingByStaffId(staff.getId());
            map.put("avgRating", avgRating != null ? 
                BigDecimal.valueOf(avgRating).setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO);
            ranking.add(map);
        }
        return ranking;
    }
}
