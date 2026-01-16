package com.rosy.main.domain.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class DiscussionRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 课程ID
     */
    @NotNull(message = "课程ID不能为空")
    @Positive(message = "课程ID必须为正整数")
    private Long courseId;

    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    @Positive(message = "用户ID必须为正整数")
    private Long userId;

    /**
     * 用户类型 1-学生 2-教师
     */
    @NotNull(message = "用户类型不能为空")
    @Min(value = 1, message = "用户类型值只能为1或2")
    @Max(value = 2, message = "用户类型值只能为1或2")
    private Byte userType;

    /**
     * 讨论内容
     */
    @NotBlank(message = "讨论内容不能为空")
    @Size(max = 1000, message = "讨论内容长度不能超过1000个字符")
    private String content;
}
