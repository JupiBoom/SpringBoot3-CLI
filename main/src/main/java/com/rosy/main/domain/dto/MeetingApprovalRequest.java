package com.rosy.main.domain.dto;

import lombok.Data;

@Data
public class MeetingApprovalRequest {

    private Long reservationId;
    private Byte approvalStatus;
    private String approvalComment;
}
