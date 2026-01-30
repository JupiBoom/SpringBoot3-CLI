package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * <p>
 * 直播间数据统计视图对象
 * </p>
 *
 * @author Rosy
 * @since 2025-01-30
 */
@Data
public class LiveRoomStatsVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;

    /**
     * 直播间ID
     */
    private Long liveRoomId;

    /**
     * 直播间标题
     */
    private String liveRoomTitle;

    /**
     * 统计日期
     */
    private LocalDate statsDate;

    /**
     * 总观众人数
     */
    private Integer totalViewers;

    /**
     * 峰值观众人数
     */
    private Integer peakViewers;

    /**
     * 平均观看时长（秒）
     */
    private Integer avgViewTime;

    /**
     * 平均观看时长（分钟）
     */
    private Double avgViewTimeMinutes;

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
}