package com.rosy.main.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 报名实体类
 */
@Data
@TableName("enrollment")
public class Enrollment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID，必须为正整数
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 活动ID，关联活动表
     */
    private Long activityId;

    /**
     * 用户ID，关联用户表
     */
    private Long userId;

    /**
     * 报名状态：0-待审核，1-审核通过，2-审核拒绝
     */
    private Integer status;

    /**
     * 申请时间
     */
    private LocalDateTime applyTime;

    /**
     * 审核时间
     */
    private LocalDateTime auditTime;

    /**
     * 审核人ID，关联用户表
     */
    private Long auditUserId;

    /**
     * 审核原因，最大长度 255
     */
    private String auditReason;

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
     * 逻辑删除字段，0 或 1
     */
    @TableLogic
    private Integer isDeleted;
}