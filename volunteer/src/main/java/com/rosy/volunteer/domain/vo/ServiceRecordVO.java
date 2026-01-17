package com.rosy.volunteer.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ServiceRecordVO {
    private Long id;
    private Long registrationId;
    private Long activityId;
    private String activityTitle;
    private Long volunteerId;
    private String volunteerName;
    private Integer serviceDuration;
    private Integer rating;
    private String ratingContent;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private String certificateUrl;
    private LocalDateTime createTime;
}
