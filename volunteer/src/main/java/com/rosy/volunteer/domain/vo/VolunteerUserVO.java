package com.rosy.volunteer.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VolunteerUserVO {
    private Long id;
    private String username;
    private String realName;
    private String phone;
    private String email;
    private String avatar;
    private String gender;
    private Integer age;
    private String skills;
    private String introduction;
    private Integer totalServiceDuration;
    private Integer activityCount;
    private LocalDateTime createTime;
    private LocalDateTime lastLoginTime;
}
