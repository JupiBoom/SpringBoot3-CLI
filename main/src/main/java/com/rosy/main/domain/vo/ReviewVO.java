package com.rosy.main.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "评价VO")
public class ReviewVO {

    @Schema(description = "评价ID")
    private Long id;

    @Schema(description = "活动ID")
    private Long activityId;

    @Schema(description = "活动标题")
    private String activityTitle;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "用户姓名")
    private String userName;

    @Schema(description = "用户头像")
    private String userAvatar;

    @Schema(description = "报名记录ID")
    private Long registrationId;

    @Schema(description = "评分：1-5星")
    private Integer rating;

    @Schema(description = "评价内容")
    private String content;

    @Schema(description = "标签，逗号分隔")
    private String tags;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
