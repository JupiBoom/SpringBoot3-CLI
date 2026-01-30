package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.main.domain.entity.LiveRoom;
import com.rosy.main.domain.entity.LiveRoomStats;
import com.rosy.main.domain.vo.LiveRoomStatsVO;
import com.rosy.main.mapper.LiveRoomStatsMapper;
import com.rosy.main.service.ILiveRoomService;
import com.rosy.main.service.ILiveRoomStatsService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 直播间数据统计表 服务实现类
 * </p>
 *
 * @author Rosy
 * @since 2025-01-30
 */
@Service
public class LiveRoomStatsServiceImpl extends ServiceImpl<LiveRoomStatsMapper, LiveRoomStats> implements ILiveRoomStatsService {

    @Resource
    private ILiveRoomService liveRoomService;

    @Override
    public LiveRoomStatsVO getLiveRoomStatsVO(LiveRoomStats liveRoomStats) {
        if (liveRoomStats == null) {
            return null;
        }
        LiveRoomStatsVO liveRoomStatsVO = new LiveRoomStatsVO();
        BeanUtil.copyProperties(liveRoomStats, liveRoomStatsVO);
        
        // 计算平均观看时长（分钟）
        if (liveRoomStats.getAvgViewTime() != null && liveRoomStats.getAvgViewTime() > 0) {
            liveRoomStatsVO.setAvgViewTimeMinutes(liveRoomStats.getAvgViewTime() / 60.0);
        }
        
        // 计算转化率百分比
        if (liveRoomStats.getConversionRate() != null) {
            liveRoomStatsVO.setConversionRatePercent(
                    liveRoomStats.getConversionRate().multiply(new BigDecimal("100"))
                            .setScale(2, RoundingMode.HALF_UP) + "%");
        }
        
        // 获取直播间标题
        LiveRoom liveRoom = liveRoomService.getById(liveRoomStats.getLiveRoomId());
        if (liveRoom != null) {
            liveRoomStatsVO.setLiveRoomTitle(liveRoom.getTitle());
        }
        
        return liveRoomStatsVO;
    }

    @Override
    public List<LiveRoomStatsVO> getStatsByDateRange(Long liveRoomId, LocalDate startDate, LocalDate endDate) {
        QueryWrapper<LiveRoomStats> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("live_room_id", liveRoomId)
                .between("stats_date", startDate, endDate)
                .orderByDesc("stats_date");
        
        List<LiveRoomStats> statsList = this.list(queryWrapper);
        
        return statsList.stream()
                .map(this::getLiveRoomStatsVO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<LiveRoomStatsVO> pageStatsByLiveRoomId(Long liveRoomId, long current, long size) {
        QueryWrapper<LiveRoomStats> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("live_room_id", liveRoomId)
                .orderByDesc("stats_date");
        
        Page<LiveRoomStats> statsPage = this.page(new Page<>(current, size), queryWrapper);
        
        Page<LiveRoomStatsVO> statsVOPage = new Page<>(current, size, statsPage.getTotal());
        statsVOPage.setRecords(statsPage.getRecords().stream()
                .map(this::getLiveRoomStatsVO)
                .collect(Collectors.toList()));
        
        return statsVOPage;
    }

    @Override
    public boolean generateDailyStats(Long liveRoomId, LocalDate statsDate) {
        // 查询是否已存在该日期的统计数据
        QueryWrapper<LiveRoomStats> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("live_room_id", liveRoomId)
                .eq("stats_date", statsDate);
        
        LiveRoomStats existingStats = this.getOne(queryWrapper);
        
        if (existingStats != null) {
            // 更新现有统计数据
            // TODO: 实现统计数据的计算逻辑
            return this.updateById(existingStats);
        } else {
            // 创建新的统计数据
            LiveRoomStats newStats = new LiveRoomStats();
            newStats.setLiveRoomId(liveRoomId);
            newStats.setStatsDate(statsDate);
            // TODO: 实现统计数据的计算逻辑
            return this.save(newStats);
        }
    }

    @Override
    public boolean updateViewerStats(Long liveRoomId, Integer totalViewers, Integer peakViewers) {
        LocalDate today = LocalDate.now();
        
        QueryWrapper<LiveRoomStats> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("live_room_id", liveRoomId)
                .eq("stats_date", today);
        
        LiveRoomStats stats = this.getOne(queryWrapper);
        
        if (stats == null) {
            // 创建新的统计数据
            stats = new LiveRoomStats();
            stats.setLiveRoomId(liveRoomId);
            stats.setStatsDate(today);
            stats.setTotalViewers(totalViewers);
            stats.setPeakViewers(peakViewers);
            return this.save(stats);
        } else {
            // 更新现有统计数据
            UpdateWrapper<LiveRoomStats> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id", stats.getId())
                    .set("total_viewers", totalViewers)
                    .set("peak_viewers", peakViewers);
            return this.update(updateWrapper);
        }
    }

    @Override
    public boolean updateSalesStats(Long liveRoomId, Integer totalOrders, BigDecimal totalSales) {
        LocalDate today = LocalDate.now();
        
        QueryWrapper<LiveRoomStats> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("live_room_id", liveRoomId)
                .eq("stats_date", today);
        
        LiveRoomStats stats = this.getOne(queryWrapper);
        
        if (stats == null) {
            // 创建新的统计数据
            stats = new LiveRoomStats();
            stats.setLiveRoomId(liveRoomId);
            stats.setStatsDate(today);
            stats.setTotalOrders(totalOrders);
            stats.setTotalSales(totalSales);
            
            // 计算转化率
            if (stats.getTotalViewers() != null && stats.getTotalViewers() > 0) {
                BigDecimal conversionRate = new BigDecimal(totalOrders)
                        .divide(new BigDecimal(stats.getTotalViewers()), 4, RoundingMode.HALF_UP);
                stats.setConversionRate(conversionRate);
            }
            
            return this.save(stats);
        } else {
            // 更新现有统计数据
            UpdateWrapper<LiveRoomStats> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id", stats.getId())
                    .set("total_orders", totalOrders)
                    .set("total_sales", totalSales);
            
            // 计算转化率
            if (stats.getTotalViewers() != null && stats.getTotalViewers() > 0) {
                BigDecimal conversionRate = new BigDecimal(totalOrders)
                        .divide(new BigDecimal(stats.getTotalViewers()), 4, RoundingMode.HALF_UP);
                updateWrapper.set("conversion_rate", conversionRate);
            }
            
            return this.update(updateWrapper);
        }
    }
}