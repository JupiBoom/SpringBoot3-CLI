package com.rosy.main.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 服务记录实体类
 */
@Data
@TableName("service_record")
public class ServiceRecord implements Serializable {

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
     * 签到时间
     */
    private LocalDateTime checkInTime;

    /**
     * 签出时间
     */
    private LocalDateTime checkOutTime;

    /**
     * 服务时长（小时）
     */
    private BigDecimal serviceHours;

    /**
     * 服务评价：1-5星
     */
    private Integer rating;

    /**
     * 服务反馈
     */
    private String feedback;

    /**
     * 服务证明编号，最大长度 50
     */
    private String certificateId;

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