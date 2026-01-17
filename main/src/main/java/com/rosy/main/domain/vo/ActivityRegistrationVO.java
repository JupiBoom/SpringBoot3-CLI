package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 活动报名VO
 */
@Data
public class ActivityRegistrationVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 活动ID
     */
    private Long activityId;

    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 志愿者ID
     */
    private Long volunteerId;

    /**
     * 志愿者姓名
     */
    private String volunteerName;

    /**
     * 报名备注
     */
    private String remark;

    /**
     * 审核状态（0-待审核，1-通过，2-拒绝）
     */
    private Integer auditStatus;

    /**
     * 审核备注
     */
    private String auditRemark;

    /**
     * 审核时间
     */
    private LocalDateTime auditTime;

    /**
     * 报名时间
     */
    private LocalDateTime createTime;

    /**
     * 服务时长（分钟）
     */
    private Integer serviceDuration;

    /**
     * 签到状态（0-未签到，1-已签到）
     */
    private Integer checkInStatus;

    /**
     * 签出状态（0-未签出，1-已签出）
     */
    private Integer checkOutStatus;

    /**
     * 签到时间
     */
    private LocalDateTime checkInTime;

    /**
     * 签出时间
     */
    private LocalDateTime checkOutTime;
}