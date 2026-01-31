package com.rosy.main.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 论坛评论实体类
 */
@Data
@TableName("forum_comment")
public class ForumComment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID，必须为正整数
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 帖子ID，关联论坛帖子表
     */
    private Long postId;

    /**
     * 用户ID，关联用户表
     */
    private Long userId;

    /**
     * 父评论ID，关联论坛评论表，可选
     */
    private Long parentId;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 点赞数
     */
    private Integer likeCount;

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