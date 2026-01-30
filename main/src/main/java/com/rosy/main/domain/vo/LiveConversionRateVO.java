package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 直播转化率统计视图对象
 * </p>
 *
 * @author Rosy
 * @since 2025-01-30
 */
@Data
public class LiveConversionRateVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 直播间ID
     */
    private Long liveRoomId;

    /**
     * 直播间标题
     */
    private String liveRoomTitle;

    /**
     * 总观众人数
     */
    private Integer totalViewers;

    /**
     * 总订单数
     */
    private Integer totalOrders;

    /**
     * 总销售额
     */
    private BigDecimal totalSales;

    /**
     * 转化率（订单数/观众数）
     */
    private BigDecimal conversionRate;

    /**
     * 转化率百分比
     */
    private String conversionRatePercent;

    /**
     * 平均客单价
     */
    private BigDecimal avgOrderValue;

    /**
     * 统计时间
     */
    private LocalDateTime statsTime;
}