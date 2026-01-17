package com.rosy.repair.domain.request;

import lombok.Data;

@Data
public class RepairRecordRequest {

    private Long orderId;

    private String repairRecord;

    private String result;

    public Long getOrderId() {
        return orderId;
    }

    public String getRepairRecord() {
        return repairRecord;
    }

    public String getResult() {
        return result;
    }
}
