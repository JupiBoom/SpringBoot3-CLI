package com.rosy.main.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 服务记录实体类
 *
 * @author Rosy
 * @since 2026-01-17
 */
@Data
@TableName("service_record")
public class ServiceRecord implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 服务记录ID
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
     * 活动名称
     */
    private String activityName;

    /**
     * 活动分类
     */
    private Byte activityCategory;

    /**
     * 签到时间
     */
    private LocalDateTime signInTime;

    /**
     * 签出时间
     */
    private LocalDateTime signOutTime;

    /**
     * 服务时长（分钟）
     */
    private Integer serviceDuration;

    /**
     * 服务评分（1-5星）
     */
    private Byte rating;

    /**
     * 服务评价内容
     */
    private String evaluation;

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
     * 是否删除：0-未删除，1-已删除
     */
    @TableLogic
    private Byte isDeleted;
}