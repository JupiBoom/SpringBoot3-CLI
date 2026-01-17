package com.rosy.main.domain.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 统计数据VO
 */
@Data
public class DashboardStatisticsVO {

    private BigDecimal avgResponseTime;

    private List<Map<String, Object>> faultTypeStatistics;

    private List<Map<String, Object>> orderTrendStatistics;

    private List<Map<String, Object>> statusStatistics;

    private List<Map<String, Object>> equipmentTypeStatistics;

    private Long totalOrders;

    private Long pendingOrders;

    private Long processingOrders;

    private Long completedOrders;

    private Double avgRating;

    private Long totalEvaluations;
}
