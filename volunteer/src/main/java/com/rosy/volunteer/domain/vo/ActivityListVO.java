package com.rosy.volunteer.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ActivityListVO {
    private Long id;
    private String title;
    private String categoryName;
    private LocalDateTime activityTime;
    private String location;
    private Integer requiredVolunteers;
    private Integer registeredCount;
    private Integer status;
    private String statusName;
    private String coverImage;
    private Integer maxDuration;
    private LocalDateTime createTime;
}
