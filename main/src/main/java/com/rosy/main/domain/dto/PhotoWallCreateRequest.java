package com.rosy.main.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 照片墙创建请求DTO
 */
@Data
public class PhotoWallCreateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 关联活动ID
     */
    private Long activityId;

    /**
     * 照片标题
     */
    @Size(max = 200, message = "照片标题长度不能超过200")
    private String title;

    /**
     * 照片描述
     */
    @Size(max = 500, message = "照片描述长度不能超过500")
    private String description;

    /**
     * 图片URL
     */
    @NotBlank(message = "图片URL不能为空")
    private String imageUrl;

    /**
     * 缩略图URL
     */
    private String thumbnailUrl;
}
