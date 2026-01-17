package com.rosy.volunteer.domain.dto;

import lombok.Data;

import javax.validation.constraints.*;

@Data
public class ServiceRatingDTO {
    @NotNull(message = "评分不能为空")
    @Min(value = 1, message = "评分最低为1星")
    @Max(value = 5, message = "评分最高为5星")
    private Integer rating;
    @NotBlank(message = "评价内容不能为空")
    @Size(min = 10, max = 1000, message = "评价内容长度在10-1000字符之间")
    private String comment;
}
