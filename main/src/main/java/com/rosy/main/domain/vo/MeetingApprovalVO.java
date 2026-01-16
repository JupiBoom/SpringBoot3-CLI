package com.rosy.main.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MeetingApprovalVO {

    private Long id;
    private Long reservationId;
    private Long approverId;
    private String approverName;
    private Byte approvalStatus;
    private String approvalStatusDesc;
    private String approvalComment;
    private LocalDateTime approvalTime;
}
