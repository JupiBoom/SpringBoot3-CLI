package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ServiceRecordVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long activityId;

    private String activityTitle;

    private Long userId;

    private String userName;

    private LocalDateTime checkInTime;

    private LocalDateTime checkOutTime;

    private BigDecimal serviceHours;

    private Byte rating;

    private String comment;

    private String certificateUrl;

    private LocalDateTime createTime;
}
