package com.rosy.web.controller.meeting;

import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.main.domain.vo.RoomStatisticsVO;
import com.rosy.main.service.IRoomStatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/statistics")
@Tag(name = "统计管理", description = "会议室使用统计")
public class StatisticsController {

    @Resource
    private IRoomStatisticsService roomStatisticsService;

    @GetMapping("/room")
    @Operation(summary = "获取会议室单日统计")
    public ApiResponse getRoomStatistics(@RequestParam Long roomId,
                                          @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") String date) {
        LocalDate localDate = LocalDate.parse(date);
        RoomStatisticsVO statistics = roomStatisticsService.getRoomStatistics(roomId, localDate);
        return ApiResponse.success(statistics);
    }

    @GetMapping("/room/range")
    @Operation(summary = "获取会议室时间段统计")
    public ApiResponse getRoomStatisticsRange(@RequestParam Long roomId,
                                               @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") String startDate,
                                               @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        List<RoomStatisticsVO> statisticsList = roomStatisticsService.getRoomStatisticsRange(roomId, start, end);
        return ApiResponse.success(statisticsList);
    }

    @GetMapping("/room/today")
    @Operation(summary = "获取会议室今日统计")
    public ApiResponse getTodayRoomStatistics(@RequestParam Long roomId) {
        LocalDate today = LocalDate.now();
        RoomStatisticsVO statistics = roomStatisticsService.getRoomStatistics(roomId, today);
        return ApiResponse.success(statistics);
    }

    @GetMapping("/room/week")
    @Operation(summary = "获取会议室本周统计")
    public ApiResponse getWeekRoomStatistics(@RequestParam Long roomId) {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(java.time.DayOfWeek.MONDAY);
        LocalDate endOfWeek = today.with(java.time.DayOfWeek.SUNDAY);
        List<RoomStatisticsVO> statisticsList = roomStatisticsService.getRoomStatisticsRange(roomId, startOfWeek, endOfWeek);
        return ApiResponse.success(statisticsList);
    }

    @GetMapping("/room/month")
    @Operation(summary = "获取会议室本月统计")
    public ApiResponse getMonthRoomStatistics(@RequestParam Long roomId) {
        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.withDayOfMonth(1);
        LocalDate endOfMonth = today.withDayOfMonth(today.lengthOfMonth());
        List<RoomStatisticsVO> statisticsList = roomStatisticsService.getRoomStatisticsRange(roomId, startOfMonth, endOfMonth);
        return ApiResponse.success(statisticsList);
    }

    @PostMapping("/generate/daily")
    @Operation(summary = "生成每日统计数据")
    public ApiResponse generateDailyStatistics(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") String date) {
        LocalDate localDate = LocalDate.parse(date);
        roomStatisticsService.generateDailyStatistics(localDate);
        return ApiResponse.success(true);
    }
}
