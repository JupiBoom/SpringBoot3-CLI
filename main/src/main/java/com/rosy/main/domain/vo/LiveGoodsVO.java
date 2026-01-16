package com.rosy.main.domain.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class LiveGoodsVO {
    private Long id;
    private Long liveRoomId;
    private Long goodsId;
    private String goodsName;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private Integer stock;
    private Integer sellCount;
    private Integer sortOrder;
    private String slogan;
    private String coverUrl;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
