package com.rosy.main.domain.dto.registration;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class RegistrationAuditRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "报名ID不能为空")
    @Positive(message = "报名ID必须为正整数")
    private Long registrationId;

    @NotNull(message = "审核状态不能为空")
    @Min(value = 1, message = "审核状态值无效")
    @Max(value = 2, message = "审核状态值无效")
    private Byte status;

    @Size(max = 255, message = "审核备注长度不能超过255个字符")
    private String auditRemark;
}
