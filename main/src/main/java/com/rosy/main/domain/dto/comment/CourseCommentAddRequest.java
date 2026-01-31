package com.rosy.main.domain.dto.comment;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class CourseCommentAddRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "课程ID不能为空")
    private Long courseId;

    @NotNull(message = "学生ID不能为空")
    private Long studentId;

    @NotBlank(message = "评论内容不能为空")
    @Size(max = 1000, message = "评论内容长度不能超过1000个字符")
    private String content;

    private Long parentId;
}
