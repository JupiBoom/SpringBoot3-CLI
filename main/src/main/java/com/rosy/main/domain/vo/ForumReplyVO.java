package com.rosy.main.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "论坛回复VO")
public class ForumReplyVO {

    @Schema(description = "回复ID")
    private Long id;

    @Schema(description = "帖子ID")
    private Long postId;

    @Schema(description = "父回复ID")
    private Long parentId;

    @Schema(description = "回复者ID")
    private Long userId;

    @Schema(description = "回复者姓名")
    private String userName;

    @Schema(description = "回复者头像")
    private String userAvatar;

    @Schema(description = "回复内容")
    private String content;

    @Schema(description = "点赞次数")
    private Integer likeCount;

    @Schema(description = "状态：0-隐藏 1-正常")
    private Integer status;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
