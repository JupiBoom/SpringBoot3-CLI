package com.rosy.main.domain.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class LiveGoodsRankVO {
    private Long id;
    private Long liveRoomId;
    private Long goodsId;
    private String goodsName;
    private Integer sellCount;
    private BigDecimal salesAmount;
    private Integer rank;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
