package com.rosy.volunteer.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ForumPostDetailVO {
    private Long id;
    private Long activityId;
    private String activityTitle;
    private Long volunteerId;
    private String volunteerName;
    private String volunteerAvatar;
    private String title;
    private String content;
    private Integer status;
    private String statusName;
    private Integer viewCount;
    private Integer likeCount;
    private Integer commentCount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private List<ForumCommentVO> comments;
}
