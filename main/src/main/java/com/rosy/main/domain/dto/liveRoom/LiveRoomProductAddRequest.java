package com.rosy.main.domain.dto.liveRoom;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 直播间商品添加请求
 * </p>
 *
 * @author Rosy
 * @since 2025-01-30
 */
@Data
public class LiveRoomProductAddRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 直播间ID
     */
    private Long liveRoomId;

    /**
     * 商品ID
     */
    private Long productId;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 商品图片URL
     */
    private String productImage;

    /**
     * 商品价格
     */
    private BigDecimal productPrice;

    /**
     * 商品卖点说明
     */
    private String sellingPoints;

    /**
     * 展示顺序
     */
    private Integer displayOrder;
}