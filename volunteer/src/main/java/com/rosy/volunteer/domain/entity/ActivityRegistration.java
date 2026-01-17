package com.rosy.volunteer.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("activity_registration")
public class ActivityRegistration {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long activityId;
    private Long volunteerId;
    private String realName;
    private String phone;
    private String email;
    private Integer age;
    private Integer gender;
    private String skills;
    private Integer status;
    private Long reviewerId;
    private LocalDateTime reviewTime;
    private String reviewNote;
    private Integer isCheckedIn;
    private LocalDateTime checkInTime;
    private Integer isCheckedOut;
    private LocalDateTime checkOutTime;
    private Long creatorId;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    private Long updaterId;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @TableLogic
    private Integer isDeleted;
}
