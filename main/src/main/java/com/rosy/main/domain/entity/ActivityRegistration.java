package com.rosy.main.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 活动报名实体类
 *
 * @author Rosy
 * @since 2026-01-17
 */
@Data
@TableName("activity_registration")
public class ActivityRegistration implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 报名ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 活动ID
     */
    private Long activityId;

    /**
     * 志愿者ID
     */
    private Long volunteerId;

    /**
     * 志愿者姓名
     */
    private String volunteerName;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 报名备注
     */
    private String remark;

    /**
     * 审核状态：0-待审核，1-已通过，2-已拒绝
     */
    private Byte auditStatus;

    /**
     * 审核备注
     */
    private String auditRemark;

    /**
     * 审核人ID
     */
    private Long auditorId;

    /**
     * 审核时间
     */
    private LocalDateTime auditTime;

    /**
     * 是否签到：0-未签到，1-已签到
     */
    private Byte isSignedIn;

    /**
     * 签到时间
     */
    private LocalDateTime signInTime;

    /**
     * 是否签出：0-未签出，1-已签出
     */
    private Byte isSignedOut;

    /**
     * 签出时间
     */
    private LocalDateTime signOutTime;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 乐观锁版本号
     */
    @Version
    private Byte version;

    /**
     * 是否删除：0-未删除，1-已删除
     */
    @TableLogic
    private Byte isDeleted;
}