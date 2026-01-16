package com.rosy.main.domain.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class LiveDataVO {
    private Long id;
    private Long liveRoomId;
    private Integer viewCount;
    private Integer peakViewCount;
    private Integer currentViewCount;
    private Integer orderCount;
    private BigDecimal salesAmount;
    private BigDecimal conversionRate;
    private Integer likeCount;
    private Integer commentCount;
    private Integer shareCount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private List<LiveGoodsRankVO> goodsRank;
    private List<LiveUserRetentionVO> retentionData;
}
