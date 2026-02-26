package com.rosy.main.domain.dto.post;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class PostAddRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Positive(message = "活动ID必须为正整数")
    private Long activityId;

    @NotBlank(message = "帖子标题不能为空")
    @Size(max = 100, message = "帖子标题长度不能超过100个字符")
    private String title;

    @NotBlank(message = "帖子内容不能为空")
    private String content;

    @NotNull(message = "帖子类型不能为空")
    @Min(value = 1, message = "帖子类型值无效")
    @Max(value = 2, message = "帖子类型值无效")
    private Byte type;
}
