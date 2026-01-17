package com.rosy.main.domain.vo.repair;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class StatisticsVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long totalOrders;

    private Long pendingOrders;

    private Long processingOrders;

    private Long completedOrders;

    private BigDecimal avgResponseTime;

    private BigDecimal avgRepairTime;

    private List<FaultTypeStatistics> faultTypeStatistics;

    private List<DeviceTypeStatistics> deviceTypeStatistics;

    private List<PriorityStatistics> priorityStatistics;

    private List<MonthlyStatistics> monthlyStatistics;

    @Data
    public static class FaultTypeStatistics {
        private String faultType;
        private Long count;
        private BigDecimal percentage;
    }

    @Data
    public static class DeviceTypeStatistics {
        private String deviceType;
        private Long count;
        private BigDecimal percentage;
    }

    @Data
    public static class PriorityStatistics {
        private Integer priority;
        private String priorityDesc;
        private Long count;
    }

    @Data
    public static class MonthlyStatistics {
        private String month;
        private Long count;
    }
}
