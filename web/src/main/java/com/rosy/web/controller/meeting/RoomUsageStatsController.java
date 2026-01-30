package com.rosy.web.controller.meeting;

import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.main.domain.vo.RoomUsageStatsVO;
import com.rosy.main.service.IRoomUsageStatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * 会议室使用统计控制器
 */
@RestController
@RequestMapping("/api/room-usage-stats")
@Tag(name = "会议室使用统计", description = "会议室使用统计相关接口")
public class RoomUsageStatsController {

    @Autowired
    private IRoomUsageStatsService roomUsageStatsService;

    @GetMapping("/room/{roomId}")
    @Operation(summary = "获取会议室统计数据", description = "获取指定会议室在指定日期范围内的统计数据")
    public ApiResponse<List<RoomUsageStatsVO>> getRoomUsageStats(
            @Parameter(description = "会议室ID") @PathVariable Long roomId,
            @Parameter(description = "开始日期") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @Parameter(description = "结束日期") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        
        List<RoomUsageStatsVO> result = roomUsageStatsService.getRoomUsageStats(roomId, startDate, endDate);
        return ApiResponse.success(result);
    }

    @GetMapping("/all")
    @Operation(summary = "获取所有会议室统计数据", description = "获取所有会议室在指定日期范围内的统计数据")
    public ApiResponse<List<RoomUsageStatsVO>> getAllRoomUsageStats(
            @Parameter(description = "开始日期") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @Parameter(description = "结束日期") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        
        List<RoomUsageStatsVO> result = roomUsageStatsService.getAllRoomUsageStats(startDate, endDate);
        return ApiResponse.success(result);
    }

    @PostMapping("/generate/daily")
    @Operation(summary = "生成单日统计数据", description = "生成指定日期的统计数据")
    public ApiResponse<String> generateDailyStats(
            @Parameter(description = "统计日期") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        
        try {
            roomUsageStatsService.generateDailyStats(date);
            return ApiResponse.success("统计数据生成成功");
        } catch (Exception e) {
            return ApiResponse.error("统计数据生成失败：" + e.getMessage());
        }
    }

    @PostMapping("/generate/range")
    @Operation(summary = "生成日期范围统计数据", description = "生成指定日期范围内的统计数据")
    public ApiResponse<String> generateRangeStats(
            @Parameter(description = "开始日期") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @Parameter(description = "结束日期") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        
        try {
            roomUsageStatsService.generateRangeStats(startDate, endDate);
            return ApiResponse.success("统计数据生成成功");
        } catch (Exception e) {
            return ApiResponse.error("统计数据生成失败：" + e.getMessage());
        }
    }
}