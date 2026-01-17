package com.rosy.main.domain.dto.activity;

import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 活动报名审核请求
 */
@Data
public class ActivityRegistrationAuditRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 审核状态（0-待审核，1-通过，2-拒绝）
     */
    @NotNull(message = "审核状态不能为空")
    private Integer auditStatus;

    /**
     * 审核备注
     */
    private String auditRemark;
}
