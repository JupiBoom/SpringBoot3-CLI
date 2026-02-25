package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.live.LiveViewerEnterRequest;
import com.rosy.main.domain.dto.live.LiveViewerLeaveRequest;
import com.rosy.main.domain.entity.LiveViewerStats;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 直播间观众数据统计表 服务类
 * </p>
 *
 * @author Rosy
 * @since 2025-02-25
 */
public interface ILiveViewerStatsService extends IService<LiveViewerStats> {

    /**
     * 观众进入直播间
     */
    Long viewerEnter(LiveViewerEnterRequest request);

    /**
     * 观众离开直播间
     */
    boolean viewerLeave(LiveViewerLeaveRequest request);

    /**
     * 获取直播间累计观看人数
     */
    Long getTotalViewers(Long roomId);

    /**
     * 获取直播间平均停留时长（秒）
     */
    Double getAvgStayDuration(Long roomId);

    /**
     * 获取直播间每分钟观众进入数据
     */
    List<Map<String, Object>> getMinuteEnterStats(Long roomId);

    /**
     * 获取直播间每分钟观众离开数据
     */
    List<Map<String, Object>> getMinuteLeaveStats(Long roomId);

    /**
     * 获取观众停留时长分布
     */
    Map<String, Object> getStayDurationDistribution(Long roomId);

    /**
     * 生成观众留存曲线数据
     */
    List<Map<String, Object>> generateRetentionCurve(Long roomId);
}
