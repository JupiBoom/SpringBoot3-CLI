package com.rosy.repair.domain.request;

import lombok.Data;

@Data
public class RepairOrderAssignRequest {

    private Long orderId;

    private Long repairerId;

    private Integer priority;

    public Long getOrderId() {
        return orderId;
    }

    public Long getRepairerId() {
        return repairerId;
    }

    public Integer getPriority() {
        return priority;
    }
}
