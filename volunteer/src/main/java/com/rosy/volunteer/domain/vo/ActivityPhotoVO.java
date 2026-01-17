package com.rosy.volunteer.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ActivityPhotoVO {
    private Long id;
    private Long activityId;
    private Long volunteerId;
    private String volunteerName;
    private String photoUrl;
    private String description;
    private Integer sortOrder;
    private Integer likeCount;
    private LocalDateTime createTime;
}
