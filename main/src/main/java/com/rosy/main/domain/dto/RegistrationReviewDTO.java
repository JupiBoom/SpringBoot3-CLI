package com.rosy.main.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.*;

@Data
@Schema(description = "报名审核DTO")
public class RegistrationReviewDTO {

    @Schema(description = "报名记录ID")
    @NotNull(message = "报名记录ID不能为空")
    private Long registrationId;

    @Schema(description = "审核状态：1-通过 2-拒绝")
    @NotNull(message = "审核状态不能为空")
    @Min(value = 1, message = "审核状态值必须是1或2")
    @Max(value = 2, message = "审核状态值必须是1或2")
    private Integer status;

    @Schema(description = "审核备注")
    @Size(max = 500, message = "审核备注不能超过500字符")
    private String reviewComment;
}
