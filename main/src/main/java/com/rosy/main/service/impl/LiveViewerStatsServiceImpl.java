package com.rosy.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.main.domain.dto.live.LiveViewerEnterRequest;
import com.rosy.main.domain.dto.live.LiveViewerLeaveRequest;
import com.rosy.main.domain.entity.LiveRoom;
import com.rosy.main.domain.entity.LiveViewerStats;
import com.rosy.main.mapper.LiveRoomMapper;
import com.rosy.main.mapper.LiveViewerStatsMapper;
import com.rosy.main.service.ILiveViewerStatsService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 * 直播间观众数据统计表 服务实现类
 * </p>
 *
 * @author Rosy
 * @since 2025-02-25
 */
@Service
public class LiveViewerStatsServiceImpl extends ServiceImpl<LiveViewerStatsMapper, LiveViewerStats> implements ILiveViewerStatsService {

    @Resource
    private LiveRoomMapper liveRoomMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long viewerEnter(LiveViewerEnterRequest request) {
        LiveViewerStats stats = new LiveViewerStats();
        BeanUtils.copyProperties(request, stats);
        stats.setEnterTime(LocalDateTime.now());

        boolean result = this.save(stats);
        if (result) {
            // 增加直播间观众人数
            liveRoomMapper.incrementViewerCount(request.getRoomId(), 1, request.getUserId());
        }
        return stats.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean viewerLeave(LiveViewerLeaveRequest request) {
        boolean result = baseMapper.updateLeaveTime(request.getRoomId(), request.getUserId(), LocalDateTime.now()) > 0;
        if (result) {
            // 减少直播间观众人数
            liveRoomMapper.decrementViewerCount(request.getRoomId(), 1, request.getUserId());
        }
        return result;
    }

    @Override
    public Long getTotalViewers(Long roomId) {
        return baseMapper.countUniqueViewers(roomId);
    }

    @Override
    public Double getAvgStayDuration(Long roomId) {
        return baseMapper.avgStayDuration(roomId);
    }

    @Override
    public List<Map<String, Object>> getMinuteEnterStats(Long roomId) {
        LiveRoom liveRoom = liveRoomMapper.selectById(roomId);
        if (liveRoom == null || liveRoom.getStartTime() == null) {
            return List.of();
        }

        LocalDateTime startTime = liveRoom.getStartTime();
        LocalDateTime endTime = liveRoom.getEndTime() != null ? liveRoom.getEndTime() : LocalDateTime.now();

        return baseMapper.selectMinuteEnterStats(roomId, startTime, endTime);
    }

    @Override
    public List<Map<String, Object>> getMinuteLeaveStats(Long roomId) {
        LiveRoom liveRoom = liveRoomMapper.selectById(roomId);
        if (liveRoom == null || liveRoom.getStartTime() == null) {
            return List.of();
        }

        LocalDateTime startTime = liveRoom.getStartTime();
        LocalDateTime endTime = liveRoom.getEndTime() != null ? liveRoom.getEndTime() : LocalDateTime.now();

        return baseMapper.selectMinuteLeaveStats(roomId, startTime, endTime);
    }

    @Override
    public Map<String, Object> getStayDurationDistribution(Long roomId) {
        return baseMapper.selectStayDurationDistribution(roomId);
    }

    @Override
    public List<Map<String, Object>> generateRetentionCurve(Long roomId) {
        LiveRoom liveRoom = liveRoomMapper.selectById(roomId);
        if (liveRoom == null || liveRoom.getStartTime() == null) {
            return List.of();
        }
        
        LocalDateTime startTime = liveRoom.getStartTime();
        LocalDateTime endTime = liveRoom.getEndTime() != null ? liveRoom.getEndTime() : LocalDateTime.now();
        
        // 获取每分钟进入和离开数据
        List<Map<String, Object>> enterStats = baseMapper.selectMinuteEnterStats(roomId, startTime, endTime);
        List<Map<String, Object>> leaveStats = baseMapper.selectMinuteLeaveStats(roomId, startTime, endTime);
        
        // 构建时间线（从直播开始到结束的每一分钟）
        List<Map<String, Object>> result = new ArrayList<>();
        
        // 将数据转换为Map方便查找
        Map<String, Long> enterMap = new HashMap<>();
        for (Map<String, Object> stat : enterStats) {
            String timeSlot = (String) stat.get("time_slot");
            Number count = (Number) stat.get("enter_count");
            enterMap.put(timeSlot, count != null ? count.longValue() : 0L);
        }
        
        Map<String, Long> leaveMap = new HashMap<>();
        for (Map<String, Object> stat : leaveStats) {
            String timeSlot = (String) stat.get("time_slot");
            Number count = (Number) stat.get("leave_count");
            leaveMap.put(timeSlot, count != null ? count.longValue() : 0L);
        }
        
        // 生成每分钟的数据点
        LocalDateTime current = startTime.withSecond(0).withNano(0);
        LocalDateTime end = endTime.withSecond(0).withNano(0);
        long currentViewerCount = 0;
        
        while (!current.isAfter(end)) {
            String timeKey = current.toString().substring(0, 16); // 格式: yyyy-MM-dd HH:mm
            
            long enterCount = enterMap.getOrDefault(timeKey, 0L);
            long leaveCount = leaveMap.getOrDefault(timeKey, 0L);
            currentViewerCount = currentViewerCount + enterCount - leaveCount;
            
            Map<String, Object> point = new HashMap<>();
            point.put("time", timeKey);
            point.put("enterCount", enterCount);
            point.put("leaveCount", leaveCount);
            point.put("currentViewerCount", Math.max(0, currentViewerCount));
            
            result.add(point);
            current = current.plusMinutes(1);
        }
        
        return result;
    }
}
