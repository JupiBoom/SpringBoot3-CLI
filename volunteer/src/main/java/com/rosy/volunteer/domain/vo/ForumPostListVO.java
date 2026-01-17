package com.rosy.volunteer.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ForumPostListVO {
    private Long id;
    private Long activityId;
    private String activityTitle;
    private Long volunteerId;
    private String volunteerName;
    private String volunteerAvatar;
    private String title;
    private Integer status;
    private String statusName;
    private Integer viewCount;
    private Integer likeCount;
    private Integer commentCount;
    private LocalDateTime createTime;
}
