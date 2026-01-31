package com.rosy.main.domain.dto.comment;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class CourseCommentUpdateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "ID不能为空")
    private Long id;

    @Size(max = 1000, message = "评论内容长度不能超过1000个字符")
    private String content;
}
