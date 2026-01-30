package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class LiveRoomConversionStatsVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long roomId;

    private Long totalViewers;

    private Integer totalOrders;

    private BigDecimal totalSales;

    private BigDecimal orderConversionRate;

    private BigDecimal averageOrderValue;

    private Integer productClickCount;

    private BigDecimal productClickRate;
}
