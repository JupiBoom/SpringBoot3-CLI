package com.rosy.repair.domain.request;

import lombok.Data;

@Data
public class EvaluationRequest {

    private Long orderId;

    private Integer star;

    private String evaluation;

    public Long getOrderId() {
        return orderId;
    }

    public Integer getStar() {
        return star;
    }

    public String getEvaluation() {
        return evaluation;
    }
}
