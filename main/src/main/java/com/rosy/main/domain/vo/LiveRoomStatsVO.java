package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 直播间统计数据视图对象
 */
@Data
public class LiveRoomStatsVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 直播间ID
     */
    private Long roomId;

    /**
     * 直播间标题
     */
    private String roomTitle;

    // ==================== 基础数据 ====================

    /**
     * 当前观众人数
     */
    private Integer viewerCount;

    /**
     * 累计观看人数
     */
    private Integer totalViewers;

    /**
     * 峰值观众人数
     */
    private Integer peakViewerCount;

    /**
     * 点赞数
     */
    private Integer likeCount;

    /**
     * 订单数
     */
    private Integer orderCount;

    /**
     * 已支付订单数
     */
    private Integer paidOrderCount;

    /**
     * 销售额
     */
    private BigDecimal salesAmount;

    // ==================== 转化数据 ====================

    /**
     * 观看转化率（下单人数/累计观看人数）
     */
    private String viewToOrderRate;

    /**
     * 支付转化率（支付订单数/下单数）
     */
    private String orderToPayRate;

    /**
     * 整体转化率（支付人数/累计观看人数）
     */
    private String overallConversionRate;

    /**
     * 客单价（销售额/支付订单数）
     */
    private BigDecimal avgOrderValue;

    // ==================== 排行榜数据 ====================

    /**
     * 商品销售排行榜
     */
    private List<LiveRoomItemVO> topSellingItems;

    // ==================== 趋势数据 ====================

    /**
     * 每分钟观众数据（用于留存曲线）
     */
    private List<Map<String, Object>> viewerTrend;

    /**
     * 每分钟销售数据
     */
    private List<Map<String, Object>> salesTrend;

    /**
     * 观众停留时长分布
     */
    private Map<String, Object> stayDurationDistribution;

    /**
     * 平均停留时长（秒）
     */
    private Double avgStayDuration;
}
