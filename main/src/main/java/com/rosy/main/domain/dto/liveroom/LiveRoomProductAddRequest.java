package com.rosy.main.domain.dto.liveroom;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class LiveRoomProductAddRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "直播间ID不能为空")
    @Positive(message = "直播间ID必须为正整数")
    private Long liveRoomId;

    @NotNull(message = "商品ID不能为空")
    @Positive(message = "商品ID必须为正整数")
    private Long productId;

    @PositiveOrZero(message = "排序必须为非负整数")
    private Integer sortOrder = 0;
}
