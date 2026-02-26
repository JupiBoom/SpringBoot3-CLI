package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
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

    private Long registrationId;

    private LocalDate serviceDate;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private BigDecimal durationHours;

    private Byte rating;

    private String comment;

    private LocalDateTime ratingTime;

    private String certificateUrl;

    private LocalDateTime createTime;
}
