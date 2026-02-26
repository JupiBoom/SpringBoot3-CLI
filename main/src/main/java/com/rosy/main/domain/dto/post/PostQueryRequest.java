package com.rosy.main.domain.dto.post;

import com.rosy.common.domain.entity.PageRequest;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class PostQueryRequest extends PageRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Positive(message = "ID必须为正整数")
    private Long id;

    @Positive(message = "活动ID必须为正整数")
    private Long activityId;

    @Positive(message = "用户ID必须为正整数")
    private Long userId;

    @Min(value = 1, message = "帖子类型值无效")
    @Max(value = 2, message = "帖子类型值无效")
    private Byte type;

    @Size(max = 50, message = "关键词长度不能超过50个字符")
    private String keyword;
}
