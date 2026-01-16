package com.rosy.main.domain.vo;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Data
public class LiveGoodsAddVO {
    @NotNull(message = "直播间ID不能为空")
    private Long liveRoomId;

    @NotNull(message = "商品ID不能为空")
    private Long goodsId;

    @NotBlank(message = "商品名称不能为空")
    private String goodsName;

    @NotNull(message = "价格不能为空")
    private BigDecimal price;

    private BigDecimal originalPrice;

    private Integer stock;
    private String slogan;
    private String coverUrl;
}
