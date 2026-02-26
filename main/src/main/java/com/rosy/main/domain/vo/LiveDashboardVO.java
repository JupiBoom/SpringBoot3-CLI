package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class LiveDashboardVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long liveRoomId;

    private String title;

    private Byte status;

    private Integer currentViewerCount;

    private Integer totalViewerCount;

    private Integer totalOrders;

    private BigDecimal totalSales;

    private BigDecimal conversionRate;

    private List<ProductRankingVO> productRankings;

    private List<ViewerRetentionPoint> retentionCurve;

    @Data
    public static class ViewerRetentionPoint implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;

        private Integer statHour;

        private Integer viewerCount;

        private Integer peakViewerCount;

        private BigDecimal retentionRate;
    }
}
