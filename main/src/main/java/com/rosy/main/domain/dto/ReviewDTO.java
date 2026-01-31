package com.rosy.main.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.*;

@Data
@Schema(description = "评价DTO")
public class ReviewDTO {

    @Schema(description = "活动ID")
    @NotNull(message = "活动ID不能为空")
    private Long activityId;

    @Schema(description = "报名记录ID")
    @NotNull(message = "报名记录ID不能为空")
    private Long registrationId;

    @Schema(description = "评分：1-5星")
    @NotNull(message = "评分不能为空")
    @Min(value = 1, message = "评分必须在1-5星之间")
    @Max(value = 5, message = "评分必须在1-5星之间")
    private Integer rating;

    @Schema(description = "评价内容")
    @Size(max = 2000, message = "评价内容不能超过2000字符")
    private String content;

    @Schema(description = "标签，逗号分隔")
    @Size(max = 200, message = "标签不能超过200字符")
    private String tags;
}
