package com.rosy.main.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 报名实体
 */
@Data
@TableName("registration")
public class Registration implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 报名ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 活动ID
     */
    private Long activityId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 报名状态：0-待审核 1-已通过 2-已拒绝 3-已取消 4-已签到 5-已签出
     */
    private Integer status;

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
     * 提醒是否发送：0-未发送 1-已发送
     */
    private Integer reminderSent;

    /**
     * 提醒发送时间
     */
    private LocalDateTime reminderTime;

    /**
     * 报名时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 乐观锁版本号
     */
    @Version
    private Integer version;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Integer isDeleted;
}
