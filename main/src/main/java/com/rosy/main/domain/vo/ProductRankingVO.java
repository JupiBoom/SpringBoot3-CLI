package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ProductRankingVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long productId;

    private String productName;

    private String coverUrl;

    private BigDecimal price;

    private Integer totalSold;

    private BigDecimal totalSales;

    private Integer orderCount;

    private Integer rank;
}
