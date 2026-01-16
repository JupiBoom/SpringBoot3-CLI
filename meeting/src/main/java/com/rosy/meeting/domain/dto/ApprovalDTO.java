package com.rosy.meeting.domain.dto;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

@Data
public class ApprovalDTO {
    @NotNull(message = "预约ID不能为空")
    private Long reservationId;
    
    @NotNull(message = "审批人ID不能为空")
    private Long approverId;
    
    @NotNull(message = "审批人姓名不能为空")
    private String approverName;
    
    @NotNull(message = "审批动作不能为空")
    private Integer action;
    
    private String reason;
}