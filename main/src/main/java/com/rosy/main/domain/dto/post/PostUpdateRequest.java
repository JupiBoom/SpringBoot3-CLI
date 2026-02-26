package com.rosy.main.domain.dto.post;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class PostUpdateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "帖子ID不能为空")
    @Positive(message = "帖子ID必须为正整数")
    private Long id;

    @Size(max = 100, message = "帖子标题长度不能超过100个字符")
    private String title;

    private String content;

    @Min(value = 1, message = "帖子类型值无效")
    @Max(value = 2, message = "帖子类型值无效")
    private Byte type;
}
