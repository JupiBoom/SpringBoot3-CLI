package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class LiveRoomProductVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long liveRoomId;

    private Long productId;

    private String productName;

    private String productCoverUrl;

    private BigDecimal productPrice;

    private Integer sortOrder;

    private Byte isExplaining;

    private LocalDateTime explainStartTime;
}
