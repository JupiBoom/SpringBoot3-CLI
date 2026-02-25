package com.rosy.main.domain.dto;

import lombok.Data;

@Data
public class RegistrationAuditDTO {
    private Long registrationId;
    private String status;
    private String auditReason;
}
