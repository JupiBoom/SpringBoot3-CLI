package com.rosy.volunteer.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ForumCommentVO {
    private Long id;
    private Long postId;
    private Long volunteerId;
    private String volunteerName;
    private String volunteerAvatar;
    private String content;
    private LocalDateTime createTime;
}
