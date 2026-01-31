package com.rosy.main.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.*;

@Data
@Schema(description = "照片DTO")
public class PhotoDTO {

    @Schema(description = "活动ID")
    @NotNull(message = "活动ID不能为空")
    private Long activityId;

    @Schema(description = "照片URL")
    @NotBlank(message = "照片URL不能为空")
    @Size(max = 500, message = "照片URL不能超过500字符")
    private String photoUrl;

    @Schema(description = "缩略图URL")
    @Size(max = 500, message = "缩略图URL不能超过500字符")
    private String thumbnailUrl;

    @Schema(description = "照片描述")
    @Size(max = 500, message = "照片描述不能超过500字符")
    private String description;

    @Schema(description = "是否封面：0-否 1-是")
    private Integer isCover;
}
