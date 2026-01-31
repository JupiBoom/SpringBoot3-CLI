package com.rosy.main.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.*;

@Data
@Schema(description = "论坛回复DTO")
public class ForumReplyDTO {

    @Schema(description = "帖子ID")
    @NotNull(message = "帖子ID不能为空")
    private Long postId;

    @Schema(description = "父回复ID，0为根回复")
    private Long parentId;

    @Schema(description = "回复内容")
    @NotBlank(message = "回复内容不能为空")
    @Size(max = 2000, message = "回复内容不能超过2000字符")
    private String content;
}
