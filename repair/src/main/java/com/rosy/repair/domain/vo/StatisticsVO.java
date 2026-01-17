package com.rosy.repair.domain.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
public class StatisticsVO {

    private Long totalOrders;

    private Long pendingOrders;

    private Long processingOrders;

    private Long completedOrders;

    private BigDecimal avgResponseTime;

    private List<Map<String, Object>> faultTypeAnalysis;

    private List<Map<String, Object>> deviceTypeAnalysis;

    private List<Map<String, Object>> priorityAnalysis;

    public void setAvgResponseTime(BigDecimal avgResponseTime) {
        this.avgResponseTime = avgResponseTime;
    }

    public void setFaultTypeAnalysis(List<Map<String, Object>> faultTypeAnalysis) {
        this.faultTypeAnalysis = faultTypeAnalysis;
    }

    public void setDeviceTypeAnalysis(List<Map<String, Object>> deviceTypeAnalysis) {
        this.deviceTypeAnalysis = deviceTypeAnalysis;
    }

    public void setPriorityAnalysis(List<Map<String, Object>> priorityAnalysis) {
        this.priorityAnalysis = priorityAnalysis;
    }

    public void setTotalOrders(Long totalOrders) {
        this.totalOrders = totalOrders;
    }

    public void setPendingOrders(Long pendingOrders) {
        this.pendingOrders = pendingOrders;
    }

    public void setProcessingOrders(Long processingOrders) {
        this.processingOrders = processingOrders;
    }

    public void setCompletedOrders(Long completedOrders) {
        this.completedOrders = completedOrders;
    }
}
