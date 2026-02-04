package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 论坛帖子VO
 */
@Data
public class ForumPostVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 帖子ID
     */
    private Long id;

    /**
     * 关联活动ID
     */
    private Long activityId;

    /**
     * 活动标题
     */
    private String activityTitle;

    /**
     * 发布者ID
     */
    private Long userId;

    /**
     * 发布者姓名
     */
    private String userName;

    /**
     * 发布者头像
     */
    private String userAvatar;

    /**
     * 帖子标题
     */
    private String title;

    /**
     * 帖子内容
     */
    private String content;

    /**
     * 帖子类型
     */
    private Integer type;

    /**
     * 帖子类型描述
     */
    private String typeDesc;

    /**
     * 图片列表
     */
    private List<String> images;

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
     * 是否置顶
     */
    private Integer isTop;

    /**
     * 是否精华
     */
    private Integer isEssence;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 状态描述
     */
    private String statusDesc;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
