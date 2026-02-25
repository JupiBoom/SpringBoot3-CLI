package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 直播间商品视图对象
 */
@Data
public class LiveRoomItemVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;

    /**
     * 直播间ID
     */
    private Long roomId;

    /**
     * 商品ID
     */
    private Long itemId;

    /**
     * 商品名称
     */
    private String itemName;

    /**
     * 商品售价
     */
    private BigDecimal itemPrice;

    /**
     * 商品主图
     */
    private String itemImage;

    /**
     * 商品卖点列表
     */
    private List<String> sellingPoints;

    /**
     * 排序顺序
     */
    private Integer sortOrder;

    /**
     * 状态：0-下架，1-上架
     */
    private Byte status;

    /**
     * 状态文本
     */
    private String statusText;

    /**
     * 直播期间销量
     */
    private Integer salesCount;

    /**
     * 直播期间销售额
     */
    private BigDecimal salesAmount;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
