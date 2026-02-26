package com.rosy.main.domain.dto.photo;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class PhotoAddRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Positive(message = "活动ID必须为正整数")
    private Long activityId;

    @NotBlank(message = "图片URL不能为空")
    @Size(max = 255, message = "图片URL长度不能超过255个字符")
    private String url;

    @Size(max = 255, message = "缩略图URL长度不能超过255个字符")
    private String thumbnailUrl;

    @Size(max = 255, message = "图片描述长度不能超过255个字符")
    private String description;
}
