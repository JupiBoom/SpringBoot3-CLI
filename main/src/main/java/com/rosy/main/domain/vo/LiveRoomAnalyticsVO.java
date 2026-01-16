package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class LiveRoomAnalyticsVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long liveRoomId;

    private String liveRoomTitle;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer totalViewers;

    private Integer totalOrders;

    private BigDecimal totalSales;

    private BigDecimal conversionRate;

    private BigDecimal avgViewDuration;

    private ViewerRetentionVO viewerRetention;
}