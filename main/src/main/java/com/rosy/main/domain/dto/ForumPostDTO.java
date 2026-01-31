package com.rosy.main.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.*;

@Data
@Schema(description = "论坛帖子DTO")
public class ForumPostDTO {

    @Schema(description = "帖子ID")
    private Long id;

    @Schema(description = "关联活动ID")
    private Long activityId;

    @Schema(description = "帖子标题")
    @NotBlank(message = "帖子标题不能为空")
    @Size(max = 200, message = "帖子标题不能超过200字符")
    private String title;

    @Schema(description = "帖子内容")
    @NotBlank(message = "帖子内容不能为空")
    @Size(max = 10000, message = "帖子内容不能超过10000字符")
    private String content;

    @Schema(description = "封面图片")
    @Size(max = 500, message = "封面图片URL不能超过500字符")
    private String coverImage;
}
