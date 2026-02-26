package com.rosy.main.domain.dto.liveroom;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class SalesRecordRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "直播间ID不能为空")
    @Positive(message = "直播间ID必须为正整数")
    private Long liveRoomId;

    @NotNull(message = "商品ID不能为空")
    @Positive(message = "商品ID必须为正整数")
    private Long productId;

    @NotBlank(message = "订单号不能为空")
    @Size(max = 100, message = "订单号长度不能超过100个字符")
    private String orderNo;

    @NotNull(message = "用户ID不能为空")
    @Positive(message = "用户ID必须为正整数")
    private Long userId;

    @NotNull(message = "购买数量不能为空")
    @Positive(message = "购买数量必须为正整数")
    private Integer quantity;

    @NotNull(message = "单价不能为空")
    @DecimalMin(value = "0.00", message = "单价不能为负数")
    private BigDecimal unitPrice;
}
