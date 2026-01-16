package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class LiveRoomDataVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long liveRoomId;

    private LocalDateTime recordTime;

    private Integer viewerCount;

    private Integer totalOrders;

    private BigDecimal totalSales;

    private Integer newViewers;
}