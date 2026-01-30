package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class LiveRoomStatsVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long roomId;

    private Long totalViewers;

    private Integer peakViewers;

    private Integer currentViewers;

    private Integer totalOrders;

    private BigDecimal totalSales;

    private Long totalLikes;

    private Integer totalComments;

    private Integer totalShares;
}
