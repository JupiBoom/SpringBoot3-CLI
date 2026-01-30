package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class LiveRoomConversionRateVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long liveRoomId;

    private Integer totalAudience;

    private Integer totalPurchases;

    private BigDecimal conversionRate;

    private String formattedConversionRate;
}