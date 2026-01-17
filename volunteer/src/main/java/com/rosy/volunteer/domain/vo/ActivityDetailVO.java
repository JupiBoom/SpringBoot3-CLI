package com.rosy.volunteer.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ActivityDetailVO {
    private Long id;
    private String title;
    private String description;
    private Long categoryId;
    private String categoryName;
    private LocalDateTime activityTime;
    private String location;
    private Integer requiredVolunteers;
    private Integer registeredCount;
    private Integer status;
    private String statusName;
    private String coverImage;
    private Integer maxDuration;
    private String organizer;
    private String contactPhone;
    private List<String> requirements;
    private List<String> benefits;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer viewCount;
    private Double avgRating;
    private Integer ratingCount;
}
