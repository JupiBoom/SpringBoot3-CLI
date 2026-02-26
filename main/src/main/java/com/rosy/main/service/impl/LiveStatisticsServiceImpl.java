package com.rosy.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.entity.AudienceRecord;
import com.rosy.main.domain.entity.LiveRoom;
import com.rosy.main.domain.entity.LiveStatistics;
import com.rosy.main.domain.entity.SalesRecord;
import com.rosy.main.domain.vo.LiveDashboardVO;
import com.rosy.main.domain.vo.ProductRankingVO;
import com.rosy.main.mapper.LiveStatisticsMapper;
import com.rosy.main.service.IAudienceRecordService;
import com.rosy.main.service.ILiveRoomService;
import com.rosy.main.service.ILiveStatisticsService;
import com.rosy.main.service.ISalesRecordService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LiveStatisticsServiceImpl extends ServiceImpl<LiveStatisticsMapper, LiveStatistics> implements ILiveStatisticsService {

    @Resource
    private ILiveRoomService liveRoomService;

    @Resource
    private IAudienceRecordService audienceRecordService;

    @Resource
    private ISalesRecordService salesRecordService;

    @Override
    public void generateHourlyStatistics(Long liveRoomId) {
        if (liveRoomId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        List<AudienceRecord> audienceRecords = audienceRecordService.getAudienceRecords(liveRoomId);

        Map<Integer, List<AudienceRecord>> groupedByHour = audienceRecords.stream()
                .collect(Collectors.groupingBy(ar -> ar.getRecordTime().getHour()));

        for (Map.Entry<Integer, List<AudienceRecord>> entry : groupedByHour.entrySet()) {
            Integer hour = entry.getKey();
            List<AudienceRecord> records = entry.getValue();

            LiveStatistics stats = new LiveStatistics();
            stats.setLiveRoomId(liveRoomId);
            stats.setStatHour(hour.byteValue());
            stats.setStatTime(LocalDateTime.now().with(LocalTime.of(hour, 0)));

            double avgViewerCount = records.stream()
                    .mapToInt(AudienceRecord::getViewerCount)
                    .average()
                    .orElse(0);
            stats.setViewerCount((int) avgViewerCount);

            int peakViewerCount = records.stream()
                    .mapToInt(AudienceRecord::getViewerCount)
                    .max()
                    .orElse(0);
            stats.setPeakViewerCount(peakViewerCount);

            int newViewerCount = records.stream()
                    .mapToInt(AudienceRecord::getNewViewerCount)
                    .sum();
            stats.setNewViewerCount(newViewerCount);

            stats.setOrderCount(0);
            stats.setSalesAmount(BigDecimal.ZERO);
            stats.setConversionRate(BigDecimal.ZERO);
            stats.setCreateTime(LocalDateTime.now());

            this.save(stats);
        }
    }

    @Override
    public BigDecimal calculateConversionRate(Long liveRoomId) {
        if (liveRoomId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        LiveRoom liveRoom = liveRoomService.getById(liveRoomId);
        if (liveRoom == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "直播间不存在");
        }

        Integer totalViewerCount = liveRoom.getTotalViewerCount();
        Integer totalOrders = liveRoom.getTotalOrders();

        if (totalViewerCount == null || totalViewerCount == 0) {
            return BigDecimal.ZERO;
        }

        return BigDecimal.valueOf(totalOrders)
                .divide(BigDecimal.valueOf(totalViewerCount), 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }

    @Override
    public List<LiveDashboardVO.ViewerRetentionPoint> getRetentionCurve(Long liveRoomId) {
        if (liveRoomId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        LambdaQueryWrapper<LiveStatistics> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LiveStatistics::getLiveRoomId, liveRoomId)
                .orderByAsc(LiveStatistics::getStatHour);

        List<LiveStatistics> statistics = this.list(queryWrapper);

        if (statistics.isEmpty()) {
            return new ArrayList<>();
        }

        int maxViewerCount = statistics.stream()
                .mapToInt(LiveStatistics::getPeakViewerCount)
                .max()
                .orElse(1);

        List<LiveDashboardVO.ViewerRetentionPoint> curve = new ArrayList<>();
        for (LiveStatistics stat : statistics) {
            LiveDashboardVO.ViewerRetentionPoint point = new LiveDashboardVO.ViewerRetentionPoint();
            point.setStatHour(stat.getStatHour().intValue());
            point.setViewerCount(stat.getViewerCount());
            point.setPeakViewerCount(stat.getPeakViewerCount());

            if (maxViewerCount > 0) {
                BigDecimal retentionRate = BigDecimal.valueOf(stat.getViewerCount())
                        .divide(BigDecimal.valueOf(maxViewerCount), 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100));
                point.setRetentionRate(retentionRate);
            } else {
                point.setRetentionRate(BigDecimal.ZERO);
            }

            curve.add(point);
        }

        return curve;
    }

    @Override
    public LiveDashboardVO getLiveDashboard(Long liveRoomId) {
        if (liveRoomId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        LiveRoom liveRoom = liveRoomService.getById(liveRoomId);
        if (liveRoom == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "直播间不存在");
        }

        LiveDashboardVO dashboard = new LiveDashboardVO();
        dashboard.setLiveRoomId(liveRoomId);
        dashboard.setTitle(liveRoom.getTitle());
        dashboard.setStatus(liveRoom.getStatus());
        dashboard.setCurrentViewerCount(liveRoom.getViewerCount());
        dashboard.setTotalViewerCount(liveRoom.getTotalViewerCount());
        dashboard.setTotalOrders(liveRoom.getTotalOrders());
        dashboard.setTotalSales(liveRoom.getTotalSales());
        dashboard.setConversionRate(calculateConversionRate(liveRoomId));

        List<ProductRankingVO> rankings = salesRecordService.getProductRanking(liveRoomId, 10);
        dashboard.setProductRankings(rankings);

        List<LiveDashboardVO.ViewerRetentionPoint> retentionCurve = getRetentionCurve(liveRoomId);
        dashboard.setRetentionCurve(retentionCurve);

        return dashboard;
    }
}
