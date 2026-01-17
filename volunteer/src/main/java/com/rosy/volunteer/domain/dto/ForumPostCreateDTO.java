package com.rosy.volunteer.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class ForumPostCreateDTO {
    @NotBlank(message = "帖子标题不能为空")
    @Size(max = 200, message = "帖子标题不能超过200个字符")
    private String title;
    @NotBlank(message = "帖子内容不能为空")
    @Size(min = 10, max = 5000, message = "帖子内容长度在10-5000字符之间")
    private String content;
    private Long activityId;
}
