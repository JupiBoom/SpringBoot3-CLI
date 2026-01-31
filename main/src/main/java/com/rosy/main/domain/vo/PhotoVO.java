package com.rosy.main.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "照片VO")
public class PhotoVO {

    @Schema(description = "照片ID")
    private Long id;

    @Schema(description = "活动ID")
    private Long activityId;

    @Schema(description = "上传者ID")
    private Long userId;

    @Schema(description = "上传者姓名")
    private String userName;

    @Schema(description = "上传者头像")
    private String userAvatar;

    @Schema(description = "照片URL")
    private String photoUrl;

    @Schema(description = "缩略图URL")
    private String thumbnailUrl;

    @Schema(description = "照片描述")
    private String description;

    @Schema(description = "是否封面：0-否 1-是")
    private Integer isCover;

    @Schema(description = "点赞次数")
    private Integer likeCount;

    @Schema(description = "状态：0-隐藏 1-正常")
    private Integer status;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
