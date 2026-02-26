package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ActivityVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String title;

    private String description;

    private Byte category;

    private String categoryDesc;

    private Byte status;

    private String statusDesc;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String location;

    private BigDecimal latitude;

    private BigDecimal longitude;

    private Integer requiredCount;

    private Integer currentCount;

    private Integer remainingCount;

    private LocalDateTime checkInStart;

    private LocalDateTime checkInEnd;

    private LocalDateTime checkOutStart;

    private LocalDateTime checkOutEnd;

    private Byte autoApprove;

    private String coverImage;

    private String contactName;

    private String contactPhone;

    private Long organizerId;

    private String organizerName;

    private LocalDateTime createTime;
}
