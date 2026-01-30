package com.rosy.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.main.domain.entity.*;
import com.rosy.main.domain.vo.RoomUsageStatsVO;
import com.rosy.main.mapper.*;
import com.rosy.main.service.IRoomUsageStatsService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 会议室使用统计Service实现类
 */
@Service
public class RoomUsageStatsServiceImpl extends ServiceImpl<RoomUsageStatsMapper, RoomUsageStats> implements IRoomUsageStatsService {

    @Autowired
    private MeetingRoomMapper meetingRoomMapper;
    
    @Autowired
    private MeetingBookingMapper meetingBookingMapper;
    
    @Autowired
    private MeetingCheckinMapper meetingCheckinMapper;

    @Override
    public List<RoomUsageStatsVO> getRoomUsageStats(Long roomId, LocalDate startDate, LocalDate endDate) {
        List<RoomUsageStats> statsList = baseMapper.findByRoomIdAndDateRange(roomId, startDate, endDate);
        
        return statsList.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomUsageStatsVO> getAllRoomUsageStats(LocalDate startDate, LocalDate endDate) {
        List<RoomUsageStats> statsList = baseMapper.findByDateRange(startDate, endDate);
        
        return statsList.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void generateDailyStats(LocalDate date) {
        // 获取所有会议室
        QueryWrapper<MeetingRoom> roomQuery = new QueryWrapper<>();
        roomQuery.eq("status", 1); // 只统计可用的会议室
        List<MeetingRoom> rooms = meetingRoomMapper.selectList(roomQuery);
        
        // 为每个会议室生成统计数据
        for (MeetingRoom room : rooms) {
            generateRoomDailyStats(room.getId(), date);
        }
    }

    @Override
    @Transactional
    public void generateRangeStats(LocalDate startDate, LocalDate endDate) {
        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            generateDailyStats(currentDate);
            currentDate = currentDate.plusDays(1);
        }
    }

    /**
     * 生成单个会议室单日统计数据
     * @param roomId 会议室ID
     * @param date 统计日期
     */
    private void generateRoomDailyStats(Long roomId, LocalDate date) {
        // 查询该会议室当天的所有已通过的预约
        String dateStr = date.toString();
        List<MeetingBooking> bookings = meetingBookingMapper.findBookingsByDateRange(roomId, dateStr, dateStr);
        
        // 计算总预约次数
        int totalBookings = bookings.size();
        
        // 计算实际使用次数（有签到的会议）
        int actualUsage = 0;
        BigDecimal totalHours = BigDecimal.ZERO;
        
        for (MeetingBooking booking : bookings) {
            // 查询该预约是否有签到记录
            QueryWrapper<MeetingCheckin> checkinQuery = new QueryWrapper<>();
            checkinQuery.eq("booking_id", booking.getId());
            Long checkinCount = meetingCheckinMapper.selectCount(checkinQuery);
            
            if (checkinCount > 0) {
                actualUsage++;
                
                // 计算使用时长（小时）
                long minutes = ChronoUnit.MINUTES.between(booking.getStartTime(), booking.getEndTime());
                BigDecimal hours = BigDecimal.valueOf(minutes).divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);
                totalHours = totalHours.add(hours);
            }
        }
        
        // 查询是否已有统计数据
        QueryWrapper<RoomUsageStats> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("room_id", roomId);
        queryWrapper.eq("stats_date", date);
        
        RoomUsageStats existingStats = this.getOne(queryWrapper);
        
        if (existingStats != null) {
            // 更新已有统计数据
            existingStats.setTotalBookings(totalBookings);
            existingStats.setActualUsage(actualUsage);
            existingStats.setTotalHours(totalHours);
            
            this.updateById(existingStats);
        } else {
            // 创建新的统计数据
            RoomUsageStats stats = new RoomUsageStats();
            stats.setRoomId(roomId);
            stats.setStatsDate(date);
            stats.setTotalBookings(totalBookings);
            stats.setActualUsage(actualUsage);
            stats.setTotalHours(totalHours);
            
            this.save(stats);
        }
    }

    /**
     * 实体转VO
     */
    private RoomUsageStatsVO convertToVO(RoomUsageStats stats) {
        RoomUsageStatsVO vo = new RoomUsageStatsVO();
        BeanUtils.copyProperties(stats, vo);
        
        // 获取会议室名称
        if (stats.getRoomId() != null) {
            MeetingRoom room = meetingRoomMapper.selectById(stats.getRoomId());
            if (room != null) {
                vo.setRoomName(room.getName());
            }
        }
        
        // 计算使用率
        if (stats.getTotalBookings() > 0) {
            BigDecimal usageRate = BigDecimal.valueOf(stats.getActualUsage())
                    .divide(BigDecimal.valueOf(stats.getTotalBookings()), 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .setScale(2, RoundingMode.HALF_UP);
            vo.setUsageRate(usageRate);
        } else {
            vo.setUsageRate(BigDecimal.ZERO);
        }
        
        // 计算平均使用时长
        if (stats.getActualUsage() > 0) {
            BigDecimal avgHours = stats.getTotalHours()
                    .divide(BigDecimal.valueOf(stats.getActualUsage()), 2, RoundingMode.HALF_UP);
            vo.setAvgHours(avgHours);
        } else {
            vo.setAvgHours(BigDecimal.ZERO);
        }
        
        return vo;
    }
}