package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 直播间商品视图对象
 * </p>
 *
 * @author Rosy
 * @since 2025-01-30
 */
@Data
public class LiveRoomProductVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;

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

    /**
     * 是否当前讲解商品：0-否，1-是
     */
    private Byte isCurrent;

    /**
     * 销售数量
     */
    private Integer salesCount;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}