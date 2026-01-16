package com.rosy.main.domain.vo;

import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.util.List;

@Data
public class LiveGoodsBatchAddVO {
    @NotNull(message = "直播间ID不能为空")
    private Long liveRoomId;

    @NotNull(message = "商品列表不能为空")
    private List<LiveGoodsAddVO> goodsList;
}
