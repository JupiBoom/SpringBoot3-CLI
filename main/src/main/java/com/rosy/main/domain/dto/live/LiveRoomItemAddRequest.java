package com.rosy.main.domain.dto.live;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 直播间添加商品请求
 */
@Data
public class LiveRoomItemAddRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 直播间ID
     */
    @NotNull(message = "直播间ID不能为空")
    private Long roomId;

    /**
     * 商品ID
     */
    @NotNull(message = "商品ID不能为空")
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
}
