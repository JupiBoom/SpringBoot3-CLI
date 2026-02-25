package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class LiveProductRankVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long productId;

    private String productName;

    private Integer soldCount;

    private BigDecimal salesAmount;

    private Integer clickCount;

    private BigDecimal conversionRate;
}
