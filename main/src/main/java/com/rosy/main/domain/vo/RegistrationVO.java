package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 报名VO
 */
@Data
public class RegistrationVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 报名ID
     */
    private Long id;

    /**
     * 活动ID
     */
    private Long activityId;

    /**
     * 活动标题
     */
    private String activityTitle;

    /**
     * 活动开始时间
     */
    private LocalDateTime activityStartTime;

    /**
     * 活动地点
     */
    private String activityLocation;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户姓名
     */
    private String userName;

    /**
     * 用户手机号
     */
    private String userPhone;

    /**
     * 报名状态
     */
    private Integer status;

    /**
     * 报名状态描述
     */
    private String statusDesc;

    /**
     * 报名留言
     */
    private String message;

    /**
     * 拒绝原因
     */
    private String rejectReason;

    /**
     * 审核人ID
     */
    private Long reviewerId;

    /**
     * 审核人姓名
     */
    private String reviewerName;

    /**
     * 审核时间
     */
    private LocalDateTime reviewTime;

    /**
     * 签到时间
     */
    private LocalDateTime checkInTime;

    /**
     * 签出时间
     */
    private LocalDateTime checkOutTime;

    /**
     * 签到地点
     */
    private String checkInLocation;

    /**
     * 签出地点
     */
    private String checkOutLocation;

    /**
     * 提醒是否发送
     */
    private Integer reminderSent;

    /**
     * 报名时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
