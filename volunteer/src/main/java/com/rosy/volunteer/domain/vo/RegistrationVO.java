package com.rosy.volunteer.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RegistrationVO {
    private Long id;
    private Long activityId;
    private String activityTitle;
    private Long volunteerId;
    private String volunteerName;
    private String volunteerPhone;
    private Integer status;
    private String statusName;
    private String remark;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private Integer serviceDuration;
    private LocalDateTime applyTime;
    private LocalDateTime reviewTime;
    private String reviewer;
}
