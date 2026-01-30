package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 观众留存曲线视图对象
 * </p>
 *
 * @author Rosy
 * @since 2025-01-30
 */
@Data
public class ViewerRetentionCurveVO implements Serializable {

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
     * 直播开始时间
     */
    private LocalDateTime startTime;

    /**
     * 直播结束时间
     */
    private LocalDateTime endTime;

    /**
     * 初始观众人数
     */
    private Integer initialViewers;

    /**
     * 峰值观众人数
     */
    private Integer peakViewers;

    /**
     * 平均观看时长（分钟）
     */
    private Double avgViewTimeMinutes;

    /**
     * 留存曲线数据点
     */
    private List<ViewerRetentionPointVO> retentionPoints;

    /**
     * 统计时间
     */
    private LocalDateTime statsTime;
}