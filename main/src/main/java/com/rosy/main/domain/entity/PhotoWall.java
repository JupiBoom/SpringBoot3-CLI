package com.rosy.main.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 照片墙实体
 */
@Data
@TableName("photo_wall")
public class PhotoWall implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 照片ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 关联活动ID
     */
    private Long activityId;

    /**
     * 上传者ID
     */
    private Long userId;

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
     * 是否精选：0-否 1-是
     */
    private Integer isFeatured;

    /**
     * 点赞数
     */
    private Integer likeCount;

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
