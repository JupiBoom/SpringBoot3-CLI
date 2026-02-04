package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 照片墙VO
 */
@Data
public class PhotoWallVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 照片ID
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
     * 上传者ID
     */
    private Long userId;

    /**
     * 上传者姓名
     */
    private String userName;

    /**
     * 上传者头像
     */
    private String userAvatar;

    /**
     * 照片标题
     */
    private String title;

    /**
     * 照片描述
     */
    private String description;

    /**
     * 图片URL
     */
    private String imageUrl;

    /**
     * 缩略图URL
     */
    private String thumbnailUrl;

    /**
     * 是否精选
     */
    private Integer isFeatured;

    /**
     * 点赞数
     */
    private Integer likeCount;

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
}
