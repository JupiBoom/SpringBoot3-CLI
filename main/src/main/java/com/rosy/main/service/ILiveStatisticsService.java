package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.LiveStatistics;
import com.rosy.main.domain.vo.LiveDashboardVO;

import java.math.BigDecimal;
import java.util.List;

public interface ILiveStatisticsService extends IService<LiveStatistics> {

    void generateHourlyStatistics(Long liveRoomId);

    BigDecimal calculateConversionRate(Long liveRoomId);

    List<LiveDashboardVO.ViewerRetentionPoint> getRetentionCurve(Long liveRoomId);

    LiveDashboardVO getLiveDashboard(Long liveRoomId);
}
