package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class LiveRoomProductVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long liveRoomId;

    private Long productId;

    private String productName;

    private String productImage;

    private BigDecimal productPrice;

    private String sellingPoint;

    private Integer sortOrder;

    private Byte status;

    private String statusText;
}