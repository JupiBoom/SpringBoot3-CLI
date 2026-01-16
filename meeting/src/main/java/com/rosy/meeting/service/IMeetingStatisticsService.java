package com.rosy.meeting.service;

import com.rosy.common.domain.entity.ApiResponse;

import java.time.LocalDate;

public interface IMeetingStatisticsService {
    ApiResponse getRoomUsageStatistics(Long roomId, LocalDate startDate, LocalDate endDate);
    
    ApiResponse getOverallStatistics();
    
    ApiResponse getUserMeetingStatistics(Long userId, LocalDate startDate, LocalDate endDate);
    
    ApiResponse getPopularRooms(int limit);
    
    ApiResponse getWeeklyStatistics();
}