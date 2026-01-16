package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 直播商品VO
 * </p>
 *
 * @author Rosy
 * @since 2026-01-16
 */
@Data
public class LiveProductVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long liveRoomId;

    private Long productId;

    private String productName;

    private String productImage;

    private BigDecimal originalPrice;

    private BigDecimal livePrice;

    private Integer stock;

    private Integer soldCount;

    private List<String> sellingPoints;

    private Integer sortOrder;

    private Byte status;

    private String statusText;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}