package com.rosy.main.domain.dto.activity;

import lombok.Data;

import java.io.Serializable;

/**
 * 活动报名查询请求
 */
@Data
public class ActivityRegistrationQueryRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 活动ID
     */
    private Long activityId;

    /**
     * 志愿者ID
     */
    private Long volunteerId;

    /**
     * 审核状态（0-待审核，1-通过，2-拒绝）
     */
    private Integer auditStatus;
}
