package com.rosy.main.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 活动实体类
 */
@Data
@TableName("activity")
public class Activity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID，必须为正整数
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 活动标题，最大长度 100
     */
    private String title;

    /**
     * 活动描述
     */
    private String description;

    /**
     * 活动分类：1-环保，2-助老，3-教育，4-医疗
     */
    private Integer category;

    /**
     * 活动状态：0-招募中，1-进行中，2-已完成，3-已取消
     */
    private Integer status;

    /**
     * 活动开始时间
     */
    private LocalDateTime startTime;

    /**
     * 活动结束时间
     */
    private LocalDateTime endTime;

    /**
     * 活动地点，最大长度 255
     */
    private String location;

    /**
     * 需求人数
     */
    private Integer requiredCount;

    /**
     * 当前报名人数
     */
    private Integer currentCount;

    /**
     * 联系人姓名，最大长度 50
     */
    private String contactName;

    /**
     * 联系人电话，最大长度 20
     */
    private String contactPhone;

    /**
     * 活动图片URL，多个用逗号分隔
     */
    private String images;

    /**
     * 创建者ID，关联用户表
     */
    private Long creatorId;

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