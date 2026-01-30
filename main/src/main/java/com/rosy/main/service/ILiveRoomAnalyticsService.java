package com.rosy.main.service;

import com.rosy.main.domain.vo.LiveConversionRateVO;
import com.rosy.main.domain.vo.ViewerRetentionCurveVO;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 直播间数据分析服务
 * </p>
 *
 * @author Rosy
 * @since 2025-01-30
 */
public interface ILiveRoomAnalyticsService {

    /**
     * 获取直播间转化率统计
     * @param liveRoomId 直播间ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 转化率统计
     */
    LiveConversionRateVO getConversionRateStats(Long liveRoomId, LocalDate startDate, LocalDate endDate);

    /**
     * 获取观众留存曲线
     * @param liveRoomId 直播间ID
     * @return 观众留存曲线
     */
    ViewerRetentionCurveVO getViewerRetentionCurve(Long liveRoomId);

    /**
     * 获取多个直播间的转化率对比
     * @param liveRoomIds 直播间ID列表
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 转化率对比列表
     */
    List<LiveConversionRateVO> getConversionRateComparison(List<Long> liveRoomIds, LocalDate startDate, LocalDate endDate);

    /**
     * 获取观众留存曲线对比
     * @param liveRoomIds 直播间ID列表
     * @return 观众留存曲线对比列表
     */
    List<ViewerRetentionCurveVO> getViewerRetentionComparison(List<Long> liveRoomIds);
}