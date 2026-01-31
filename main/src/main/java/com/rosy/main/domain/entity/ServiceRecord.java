package com.rosy.main.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("service_record")
public class ServiceRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long registrationId;

    private Long activityId;

    private Long userId;

    private LocalDate serviceDate;

    private LocalDateTime checkInTime;

    private LocalDateTime checkOutTime;

    private Double serviceHours;

    private String certificateNo;

    private String certificateUrl;

    private LocalDateTime generateTime;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableLogic
    private Integer isDeleted;
}
