package com.rosy.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rosy.main.domain.entity.LiveRoom;
import com.rosy.main.domain.entity.LiveRoomOrder;
import com.rosy.main.domain.entity.LiveRoomStats;
import com.rosy.main.domain.entity.LiveRoomViewer;
import com.rosy.main.domain.vo.LiveConversionRateVO;
import com.rosy.main.domain.vo.ViewerRetentionCurveVO;
import com.rosy.main.domain.vo.ViewerRetentionPointVO;
import com.rosy.main.mapper.LiveRoomOrderMapper;
import com.rosy.main.mapper.LiveRoomViewerMapper;
import com.rosy.main.service.ILiveRoomAnalyticsService;
import com.rosy.main.service.ILiveRoomService;
import com.rosy.main.service.ILiveRoomStatsService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 直播间数据分析服务实现类
 * </p>
 *
 * @author Rosy
 * @since 2025-01-30
 */
@Service
public class LiveRoomAnalyticsServiceImpl implements ILiveRoomAnalyticsService {

    @Resource
    private ILiveRoomService liveRoomService;
    
    @Resource
    private ILiveRoomStatsService liveRoomStatsService;
    
    @Resource
    private LiveRoomOrderMapper liveRoomOrderMapper;
    
    @Resource
    private LiveRoomViewerMapper liveRoomViewerMapper;

    @Override
    public LiveConversionRateVO getConversionRateStats(Long liveRoomId, LocalDate startDate, LocalDate endDate) {
        // 获取直播间信息
        LiveRoom liveRoom = liveRoomService.getById(liveRoomId);
        if (liveRoom == null) {
            return null;
        }

        LiveConversionRateVO conversionRateVO = new LiveConversionRateVO();
        conversionRateVO.setLiveRoomId(liveRoomId);
        conversionRateVO.setLiveRoomTitle(liveRoom.getTitle());
        conversionRateVO.setStatsTime(LocalDateTime.now());

        // 查询指定日期范围内的统计数据
        List<LiveRoomStats> statsList = new ArrayList<>();
        if (startDate != null && endDate != null) {
            QueryWrapper<LiveRoomStats> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("live_room_id", liveRoomId)
                    .between("stats_date", startDate, endDate);
            statsList = liveRoomStatsService.list(queryWrapper);
        }

        // 计算总观众数和总订单数
        Integer totalViewers = 0;
        Integer totalOrders = 0;
        BigDecimal totalSales = BigDecimal.ZERO;

        for (LiveRoomStats stats : statsList) {
            totalViewers += stats.getTotalViewers() != null ? stats.getTotalViewers() : 0;
            totalOrders += stats.getTotalOrders() != null ? stats.getTotalOrders() : 0;
            totalSales = totalSales.add(stats.getTotalSales() != null ? stats.getTotalSales() : BigDecimal.ZERO);
        }

        // 如果没有指定日期范围，则查询所有订单和观众数据
        if (startDate == null || endDate == null) {
            // 查询所有订单
            QueryWrapper<LiveRoomOrder> orderQueryWrapper = new QueryWrapper<>();
            orderQueryWrapper.eq("live_room_id", liveRoomId);
            List<LiveRoomOrder> orders = liveRoomOrderMapper.selectList(orderQueryWrapper);
            
            totalOrders = orders.size();
            for (LiveRoomOrder order : orders) {
                totalSales = totalSales.add(order.getTotalAmount() != null ? order.getTotalAmount() : BigDecimal.ZERO);
            }

            // 查询观众数据
            QueryWrapper<LiveRoomViewer> viewerQueryWrapper = new QueryWrapper<>();
            viewerQueryWrapper.eq("live_room_id", liveRoomId);
            totalViewers = Math.toIntExact(liveRoomViewerMapper.selectCount(viewerQueryWrapper));
        }

        conversionRateVO.setTotalViewers(totalViewers);
        conversionRateVO.setTotalOrders(totalOrders);
        conversionRateVO.setTotalSales(totalSales);

        // 计算转化率
        if (totalViewers > 0 && totalOrders > 0) {
            BigDecimal conversionRate = new BigDecimal(totalOrders)
                    .divide(new BigDecimal(totalViewers), 4, RoundingMode.HALF_UP);
            conversionRateVO.setConversionRate(conversionRate);
            conversionRateVO.setConversionRatePercent(conversionRate.multiply(new BigDecimal("100"))
                    .setScale(2, RoundingMode.HALF_UP) + "%");
        } else {
            conversionRateVO.setConversionRate(BigDecimal.ZERO);
            conversionRateVO.setConversionRatePercent("0.00%");
        }

        // 计算平均客单价
        if (totalOrders > 0) {
            BigDecimal avgOrderValue = totalSales.divide(new BigDecimal(totalOrders), 2, RoundingMode.HALF_UP);
            conversionRateVO.setAvgOrderValue(avgOrderValue);
        } else {
            conversionRateVO.setAvgOrderValue(BigDecimal.ZERO);
        }

        return conversionRateVO;
    }

    @Override
    public ViewerRetentionCurveVO getViewerRetentionCurve(Long liveRoomId) {
        // 获取直播间信息
        LiveRoom liveRoom = liveRoomService.getById(liveRoomId);
        if (liveRoom == null) {
            return null;
        }

        ViewerRetentionCurveVO retentionCurveVO = new ViewerRetentionCurveVO();
        retentionCurveVO.setLiveRoomId(liveRoomId);
        retentionCurveVO.setLiveRoomTitle(liveRoom.getTitle());
        retentionCurveVO.setStartTime(liveRoom.getStartTime());
        retentionCurveVO.setEndTime(liveRoom.getEndTime());
        retentionCurveVO.setStatsTime(LocalDateTime.now());

        // 查询观众记录
        QueryWrapper<LiveRoomViewer> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("live_room_id", liveRoomId)
                .orderByAsc("join_time");
        List<LiveRoomViewer> viewers = liveRoomViewerMapper.selectList(queryWrapper);

        if (viewers.isEmpty()) {
            retentionCurveVO.setInitialViewers(0);
            retentionCurveVO.setPeakViewers(0);
            retentionCurveVO.setAvgViewTimeMinutes(0.0);
            retentionCurveVO.setRetentionPoints(new ArrayList<>());
            return retentionCurveVO;
        }

        // 计算初始观众数（前5分钟的平均值）
        LocalDateTime startTime = liveRoom.getStartTime();
        LocalDateTime fiveMinutesAfterStart = startTime.plusMinutes(5);
        
        int initialViewers = 0;
        int totalDuration = 0;
        int peakViewers = 0;
        
        Map<Integer, Integer> timePointViewers = new HashMap<>();
        
        for (LiveRoomViewer viewer : viewers) {
            // 计算观看时长
            if (viewer.getTotalDuration() != null) {
                totalDuration += viewer.getTotalDuration();
            }
            
            // 计算每分钟的在线人数
            LocalDateTime joinTime = viewer.getJoinTime();
            LocalDateTime leaveTime = viewer.getLeaveTime() != null ? viewer.getLeaveTime() : LocalDateTime.now();
            
            // 计算从直播开始到当前时间点的分钟数
            long minutesFromStart = java.time.Duration.between(startTime, joinTime).toMinutes();
            
            // 记录每分钟的在线人数
            for (int minute = (int) minutesFromStart; minute < 60; minute++) { // 只统计前60分钟
                LocalDateTime checkTime = startTime.plusMinutes(minute);
                if (joinTime.isBefore(checkTime) && (leaveTime == null || leaveTime.isAfter(checkTime))) {
                    timePointViewers.put(minute, timePointViewers.getOrDefault(minute, 0) + 1);
                }
            }
            
            // 统计初始观众数（前5分钟进入的观众）
            if (joinTime.isBefore(fiveMinutesAfterStart)) {
                initialViewers++;
            }
        }
        
        // 计算峰值观众数
        peakViewers = timePointViewers.values().stream().mapToInt(Integer::intValue).max().orElse(0);
        
        // 计算平均观看时长
        double avgViewTimeMinutes = viewers.isEmpty() ? 0.0 : (double) totalDuration / viewers.size() / 60;
        
        retentionCurveVO.setInitialViewers(initialViewers);
        retentionCurveVO.setPeakViewers(peakViewers);
        retentionCurveVO.setAvgViewTimeMinutes(avgViewTimeMinutes);
        
        // 生成留存曲线数据点
        List<ViewerRetentionPointVO> retentionPoints = new ArrayList<>();
        for (int minute = 0; minute < 60; minute += 5) { // 每5分钟一个数据点
            ViewerRetentionPointVO point = new ViewerRetentionPointVO();
            point.setTimePoint(minute);
            point.setOnlineViewers(timePointViewers.getOrDefault(minute, 0));
            
            // 计算留存率
            double retentionRate = initialViewers > 0 ? 
                    (double) point.getOnlineViewers() / initialViewers * 100 : 0.0;
            point.setRetentionRate(retentionRate);
            point.setRetentionRatePercent(String.format("%.2f%%", retentionRate));
            point.setTimestamp(startTime.plusMinutes(minute));
            
            retentionPoints.add(point);
        }
        
        retentionCurveVO.setRetentionPoints(retentionPoints);
        
        return retentionCurveVO;
    }

    @Override
    public List<LiveConversionRateVO> getConversionRateComparison(List<Long> liveRoomIds, LocalDate startDate, LocalDate endDate) {
        return liveRoomIds.stream()
                .map(liveRoomId -> getConversionRateStats(liveRoomId, startDate, endDate))
                .collect(Collectors.toList());
    }

    @Override
    public List<ViewerRetentionCurveVO> getViewerRetentionComparison(List<Long> liveRoomIds) {
        return liveRoomIds.stream()
                .map(this::getViewerRetentionCurve)
                .collect(Collectors.toList());
    }
}