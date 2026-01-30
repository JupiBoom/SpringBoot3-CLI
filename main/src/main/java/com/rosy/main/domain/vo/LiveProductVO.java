package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class LiveProductVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long roomId;

    private Long productId;

    private String productName;

    private String productImage;

    private BigDecimal originalPrice;

    private BigDecimal livePrice;

    private List<String> sellingPoints;

    private Integer sortOrder;

    private Byte isExplaining;
}
