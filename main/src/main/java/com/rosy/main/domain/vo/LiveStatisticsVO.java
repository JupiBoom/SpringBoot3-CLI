package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class LiveStatisticsVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long roomId;

    private Integer totalOrderNum;

    private BigDecimal totalSales;

    private Integer totalViewer;

    private Integer peakViewer;

    private Integer avgOnlineTime;

    private BigDecimal conversionRate;

    private List<LiveProductRankVO> productRanks;

    private List<LiveAudienceRetentionVO> retentionData;
}
