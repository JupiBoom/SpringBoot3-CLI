package com.rosy.volunteer.domain.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class RegistrationReviewDTO {
    @NotNull(message = "审核状态不能为空")
    @Min(value = 2, message = "审核状态值不正确")
    private Integer status;
    @Size(max = 500, message = "审核备注不能超过500个字符")
    private String reviewNote;
}
