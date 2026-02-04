package com.rosy.main.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 论坛评论创建请求DTO
 */
@Data
public class ForumCommentCreateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 帖子ID
     */
    @NotNull(message = "帖子ID不能为空")
    private Long postId;

    /**
     * 父评论ID（回复功能，可选）
     */
    private Long parentId;

    /**
     * 评论内容
     */
    @NotBlank(message = "评论内容不能为空")
    @Size(max = 1000, message = "评论内容长度不能超过1000")
    private String content;
}
