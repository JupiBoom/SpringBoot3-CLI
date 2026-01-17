package com.rosy.volunteer.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ServiceRecordListVO {
    private Long id;
    private String activityTitle;
    private LocalDateTime activityTime;
    private Integer serviceDuration;
    private Integer rating;
    private LocalDateTime createTime;
}
