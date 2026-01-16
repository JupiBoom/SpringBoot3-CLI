package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 直播统计VO
 * </p>
 *
 * @author Rosy
 * @since 2026-01-16
 */
@Data
public class LiveStatsVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单数
     */
    private Integer totalOrders;

    /**
     * 销售额
     */
    private BigDecimal totalSales;

    /**
     * 已支付订单数
     */
    private Integer paidOrders;

    /**
     * 已支付金额
     */
    private BigDecimal paidSales;

    /**
     * 商品排行榜
     */
    private List<Map<String, Object>> productRanking;

    /**
     * 观众人数趋势
     */
    private List<Map<String, Object>> viewerTrend;

    /**
     * 平均留存率
     */
    private BigDecimal averageRetentionRate;

    /**
     * 留存曲线数据
     */
    private List<Map<String, Object>> retentionCurve;

    /**
     * 转化率
     */
    private BigDecimal conversionRate;

    /**
     * 商品点击次数
     */
    private Integer productClickCount;

    /**
     * 互动次数
     */
    private Integer interactionCount;
}