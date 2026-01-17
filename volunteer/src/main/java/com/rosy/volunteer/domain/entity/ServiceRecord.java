package com.rosy.volunteer.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("service_record")
public class ServiceRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long activityId;
    private Long volunteerId;
    private Long registrationId;
    private LocalDate serviceDate;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private Integer duration;
    private Integer rating;
    private String comment;
    private LocalDateTime commentTime;
    private String certificateUrl;
    private Long creatorId;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    private Long updaterId;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @TableLogic
    private Integer isDeleted;
}
