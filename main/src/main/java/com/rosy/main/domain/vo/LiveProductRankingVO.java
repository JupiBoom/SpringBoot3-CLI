package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class LiveProductRankingVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long productId;

    private String productName;

    private Integer clickCount;

    private Integer orderCount;

    private BigDecimal salesAmount;

    private BigDecimal conversionRate;
}
