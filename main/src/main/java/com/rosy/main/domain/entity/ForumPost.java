package com.rosy.main.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 论坛帖子实体
 */
@Data
@TableName("forum_post")
public class ForumPost implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 帖子ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 关联活动ID（可选）
     */
    private Long activityId;

    /**
     * 发布者ID
     */
    private Long userId;

    /**
     * 帖子标题
     */
    private String title;

    /**
     * 帖子内容
     */
    private String content;

    /**
     * 帖子类型：1-经验分享 2-活动回顾 3-问题咨询 4-其他
     */
    private Integer type;

    /**
     * 图片列表JSON数组
     */
    private String images;

    /**
     * 浏览次数
     */
    private Integer viewCount;

    /**
     * 点赞数
     */
    private Integer likeCount;

    /**
     * 评论数
     */
    private Integer commentCount;

    /**
     * 是否置顶：0-否 1-是
     */
    private Integer isTop;

    /**
     * 是否精华：0-否 1-是
     */
    private Integer isEssence;

    /**
     * 状态：0-待审核 1-已通过 2-已拒绝
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Integer isDeleted;
}
