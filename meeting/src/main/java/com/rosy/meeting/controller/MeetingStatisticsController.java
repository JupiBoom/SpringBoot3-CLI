package com.rosy.meeting.controller;

import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.meeting.service.IMeetingStatisticsService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/meeting/statistics")
public class MeetingStatisticsController {
    private final IMeetingStatisticsService statisticsService;

    public MeetingStatisticsController(IMeetingStatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("/room/{roomId}")
    public ApiResponse getRoomUsageStatistics(@PathVariable Long roomId,
                                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return statisticsService.getRoomUsageStatistics(roomId, startDate, endDate);
    }

    @GetMapping("/overview")
    public ApiResponse getOverallStatistics() {
        return statisticsService.getOverallStatistics();
    }

    @GetMapping("/user/{userId}")
    public ApiResponse getUserMeetingStatistics(@PathVariable Long userId,
                                                  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return statisticsService.getUserMeetingStatistics(userId, startDate, endDate);
    }

    @GetMapping("/popular-rooms")
    public ApiResponse getPopularRooms(@RequestParam(defaultValue = "10") int limit) {
        return statisticsService.getPopularRooms(limit);
    }

    @GetMapping("/weekly")
    public ApiResponse getWeeklyStatistics() {
        return statisticsService.getWeeklyStatistics();
    }
}