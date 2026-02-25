package com.rosy.main.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("service_record")
public class ServiceRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long activityId;
    private Long registrationId;
    private Long userId;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private BigDecimal serviceHours;
    private Integer rating;
    private String comment;
    private Integer certificateGenerated;
    private String certificateUrl;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @TableLogic
    private Integer isDeleted;
}
