package com.rosy.main.domain.dto.live;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 创建直播订单请求
 */
@Data
public class LiveOrderAddRequest implements Serializable {

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
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    /**
     * 购买数量
     */
    @NotNull(message = "购买数量不能为空")
    private Integer quantity;

    /**
     * 单价
     */
    @NotNull(message = "单价不能为空")
    private BigDecimal unitPrice;
}
