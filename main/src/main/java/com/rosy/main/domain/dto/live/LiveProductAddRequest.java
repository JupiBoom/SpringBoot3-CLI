package com.rosy.main.domain.dto.live;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 直播商品添加请求
 * </p>
 *
 * @author Rosy
 * @since 2026-01-16
 */
@Data
public class LiveProductAddRequest implements Serializable {

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
     * 商品图片
     */
    private String productImage;

    /**
     * 商品原价
     */
    private BigDecimal originalPrice;

    /**
     * 直播专属价格
     */
    private BigDecimal livePrice;

    /**
     * 库存
     */
    private Integer stock;

    /**
     * 商品卖点
     */
    private List<String> sellingPoints;

    /**
     * 排序
     */
    private Integer sortOrder;
}