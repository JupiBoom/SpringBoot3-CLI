package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
public class RepairStatisticsVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long totalOrders;

    private Long pendingOrders;

    private Long processingOrders;

    private Long completedOrders;

    private BigDecimal avgResponseMinutes;

    private BigDecimal avgRepairMinutes;

    private BigDecimal avgRating;

    private List<Map<String, Object>> faultTypeStatistics;

    private List<Map<String, Object>> orderStatusStatistics;

    private List<Map<String, Object>> monthlyOrderTrend;

    private List<Map<String, Object>> staffRanking;
}
