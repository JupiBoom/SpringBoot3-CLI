package com.rosy.main.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.*;

@Data
@Schema(description = "报名DTO")
public class RegistrationDTO {

    @Schema(description = "活动ID")
    @NotNull(message = "活动ID不能为空")
    private Long activityId;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "用户姓名")
    @NotBlank(message = "姓名不能为空")
    @Size(max = 100, message = "姓名不能超过100字符")
    private String userName;

    @Schema(description = "用户电话")
    @NotBlank(message = "电话不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "电话格式不正确")
    private String userPhone;

    @Schema(description = "报名理由")
    @Size(max = 1000, message = "报名理由不能超过1000字符")
    private String reason;
}
