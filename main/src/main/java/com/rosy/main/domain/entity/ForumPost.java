package com.rosy.main.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 论坛帖子实体类
 */
@Data
@TableName("forum_post")
public class ForumPost implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID，必须为正整数
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 关联活动ID，关联活动表，可选
     */
    private Long activityId;

    /**
     * 用户ID，关联用户表
     */
    private Long userId;

    /**
     * 帖子标题，最大长度 100
     */
    private String title;

    /**
     * 帖子内容
     */
    private String content;

    /**
     * 帖子图片URL，多个用逗号分隔
     */
    private String images;

    /**
     * 点赞数
     */
    private Integer likeCount;

    /**
     * 浏览数
     */
    private Integer viewCount;

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