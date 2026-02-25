package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ProductVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private String coverUrl;

    private BigDecimal price;

    private BigDecimal originalPrice;

    private Integer stock;

    private Integer soldCount;

    private String sellingPoints;

    private String description;

    private Byte status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
