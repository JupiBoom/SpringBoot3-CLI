package com.rosy.meeting.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.meeting.domain.entity.CheckInRecord;
import com.rosy.meeting.domain.entity.MeetingReservation;
import com.rosy.meeting.domain.entity.MeetingRoom;
import com.rosy.meeting.mapper.CheckInRecordMapper;
import com.rosy.meeting.mapper.MeetingReservationMapper;
import com.rosy.meeting.mapper.MeetingRoomMapper;
import com.rosy.meeting.service.IMeetingStatisticsService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MeetingStatisticsServiceImpl implements IMeetingStatisticsService {
    private final MeetingRoomMapper meetingRoomMapper;
    private final MeetingReservationMapper reservationMapper;
    private final CheckInRecordMapper checkInRecordMapper;

    public MeetingStatisticsServiceImpl(MeetingRoomMapper meetingRoomMapper,
                                       MeetingReservationMapper reservationMapper,
                                       CheckInRecordMapper checkInRecordMapper) {
        this.meetingRoomMapper = meetingRoomMapper;
        this.reservationMapper = reservationMapper;
        this.checkInRecordMapper = checkInRecordMapper;
    }

    @Override
    public ApiResponse getRoomUsageStatistics(Long roomId, LocalDate startDate, LocalDate endDate) {
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(23, 59, 59);
        
        LambdaQueryWrapper<MeetingReservation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MeetingReservation::getRoomId, roomId);
        wrapper.ge(MeetingReservation::getStartTime, start);
        wrapper.le(MeetingReservation::getEndTime, end);
        
        List<MeetingReservation> reservations = reservationMapper.selectList(wrapper);
        
        int totalCount = reservations.size();
        int completedCount = (int) reservations.stream()
            .filter(r -> r.getStatus() == 4)
            .count();
        int cancelledCount = (int) reservations.stream()
            .filter(r -> r.getStatus() == 3)
            .count();
        
        BigDecimal totalHours = reservations.stream()
            .filter(r -> r.getStatus() == 4)
            .map(r -> {
                long minutes = java.time.Duration.between(r.getStartTime(), r.getEndTime()).toMinutes();
                return BigDecimal.valueOf(minutes).divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);
            })
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("roomId", roomId);
        stats.put("startDate", startDate);
        stats.put("endDate", endDate);
        stats.put("totalCount", totalCount);
        stats.put("completedCount", completedCount);
        stats.put("cancelledCount", cancelledCount);
        stats.put("totalHours", totalHours);
        stats.put("occupancyRate", totalHours.divide(
            BigDecimal.valueOf((endDate.toEpochDay() - startDate.toEpochDay() + 1) * 8), 
            4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)));
        
        return ApiResponse.success(stats);
    }

    @Override
    public ApiResponse getOverallStatistics() {
        List<MeetingRoom> rooms = meetingRoomMapper.selectList(null);
        List<MeetingReservation> reservations = reservationMapper.selectList(null);
        List<CheckInRecord> checkIns = checkInRecordMapper.selectList(null);
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalRooms", rooms.size());
        stats.put("availableRooms", rooms.stream().filter(r -> r.getStatus() == 1).count());
        stats.put("totalReservations", reservations.size());
        stats.put("pendingReservations", reservations.stream().filter(r -> r.getStatus() == 0).count());
        stats.put("approvedReservations", reservations.stream().filter(r -> r.getStatus() == 1).count());
        stats.put("rejectedReservations", reservations.stream().filter(r -> r.getStatus() == 2).count());
        stats.put("completedReservations", reservations.stream().filter(r -> r.getStatus() == 4).count());
        stats.put("totalCheckIns", checkIns.size());
        
        return ApiResponse.success(stats);
    }

    @Override
    public ApiResponse getUserMeetingStatistics(Long userId, LocalDate startDate, LocalDate endDate) {
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(23, 59, 59);
        
        LambdaQueryWrapper<MeetingReservation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MeetingReservation::getApplicantId, userId);
        wrapper.ge(MeetingReservation::getStartTime, start);
        wrapper.le(MeetingReservation::getEndTime, end);
        
        List<MeetingReservation> reservations = reservationMapper.selectList(wrapper);
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("userId", userId);
        stats.put("startDate", startDate);
        stats.put("endDate", endDate);
        stats.put("totalReservations", reservations.size());
        stats.put("approvedReservations", reservations.stream().filter(r -> r.getStatus() == 1 || r.getStatus() == 4).count());
        stats.put("rejectedReservations", reservations.stream().filter(r -> r.getStatus() == 2).count());
        stats.put("cancelledReservations", reservations.stream().filter(r -> r.getStatus() == 3).count());
        
        return ApiResponse.success(stats);
    }

    @Override
    public ApiResponse getPopularRooms(int limit) {
        LocalDate oneMonthAgo = LocalDate.now().minusMonths(1);
        LocalDateTime start = oneMonthAgo.atStartOfDay();
        
        LambdaQueryWrapper<MeetingReservation> wrapper = new LambdaQueryWrapper<>();
        wrapper.ge(MeetingReservation::getStartTime, start);
        wrapper.in(MeetingReservation::getStatus, Arrays.asList(1, 4));
        
        List<MeetingReservation> reservations = reservationMapper.selectList(wrapper);
        
        Map<Long, Long> roomUsage = reservations.stream()
            .collect(Collectors.groupingBy(MeetingReservation::getRoomId, Collectors.counting()));
        
        List<Map.Entry<Long, Long>> sorted = roomUsage.entrySet().stream()
            .sorted(Map.Entry.<Long, Long>comparingByValue().reversed())
            .limit(limit)
            .collect(Collectors.toList());
        
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<Long, Long> entry : sorted) {
            MeetingRoom room = meetingRoomMapper.selectById(entry.getKey());
            if (room != null) {
                Map<String, Object> item = new HashMap<>();
                item.put("roomId", room.getId());
                item.put("roomName", room.getRoomName());
                item.put("usageCount", entry.getValue());
                result.add(item);
            }
        }
        
        return ApiResponse.success(result);
    }

    @Override
    public ApiResponse getWeeklyStatistics() {
        LocalDate today = LocalDate.now();
        LocalDate weekStart = today.with(DayOfWeek.MONDAY);
        LocalDate weekEnd = today.with(DayOfWeek.SUNDAY);
        
        Map<LocalDate, Long> dailyStats = new HashMap<>();
        for (LocalDate date = weekStart; !date.isAfter(weekEnd); date = date.plusDays(1)) {
            LocalDateTime start = date.atStartOfDay();
            LocalDateTime end = date.atTime(23, 59, 59);
            
            LambdaQueryWrapper<MeetingReservation> wrapper = new LambdaQueryWrapper<>();
            wrapper.ge(MeetingReservation::getStartTime, start);
            wrapper.le(MeetingReservation::getEndTime, end);
            wrapper.in(MeetingReservation::getStatus, Arrays.asList(1, 4));
            
            Long count = reservationMapper.selectCount(wrapper);
            dailyStats.put(date, count);
        }
        
        return ApiResponse.success(dailyStats);
    }
}