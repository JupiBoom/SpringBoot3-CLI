package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 商品销售排行榜视图对象
 * </p>
 *
 * @author Rosy
 * @since 2025-01-30
 */
@Data
public class ProductSalesRankingVO implements Serializable {

    private static final long serialVersionUID = 1L;

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
     * 销售数量
     */
    private Integer salesCount;

    /**
     * 销售总额
     */
    private BigDecimal totalSalesAmount;

    /**
     * 排名
     */
    private Integer ranking;

    /**
     * 直播间ID
     */
    private Long liveRoomId;

    /**
     * 直播间标题
     */
    private String liveRoomTitle;

    /**
     * 统计时间
     */
    private LocalDateTime statsTime;
}