package com.rosy.web.controller.repair;

import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.main.service.IRepairStatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 报修统计控制器
 *
 * @author Rosy
 * @since 2025-01-31
 */
@RestController
@RequestMapping("/repair/statistics")
@Tag(name = "报修统计", description = "报修统计相关接口")
public class RepairStatisticsController {

    @Resource
    private IRepairStatisticsService repairStatisticsService;

    /**
     * 获取报修响应时间统计
     */
    @GetMapping("/response-time")
    @Operation(summary = "获取报修响应时间统计", description = "获取报修响应时间、维修时间和总时间的平均统计")
    public ApiResponse<Map<String, Object>> getResponseTimeStatistics(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        Map<String, Object> statistics = repairStatisticsService.getResponseTimeStatistics(startDate, endDate);
        return ApiResponse.success(statistics);
    }

    /**
     * 获取设备故障类型分析
     */
    @GetMapping("/fault-type-analysis")
    @Operation(summary = "获取设备故障类型分析", description = "获取各故障类型的工单数量统计")
    public ApiResponse<List<Map<String, Object>>> getFaultTypeAnalysis(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<Map<String, Object>> analysis = repairStatisticsService.getFaultTypeAnalysis(startDate, endDate);
        return ApiResponse.success(analysis);
    }

    /**
     * 获取工单数量和类型统计
     */
    @GetMapping("/order-statistics")
    @Operation(summary = "获取工单数量和类型统计", description = "获取工单状态和优先级的统计信息")
    public ApiResponse<Map<String, Object>> getOrderStatistics(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        Map<String, Object> statistics = repairStatisticsService.getOrderStatistics(startDate, endDate);
        return ApiResponse.success(statistics);
    }

    /**
     * 获取维修人员工作量统计
     */
    @GetMapping("/technician-workload")
    @Operation(summary = "获取维修人员工作量统计", description = "获取各维修人员的工单数量和维修时间统计")
    public ApiResponse<List<Map<String, Object>>> getTechnicianWorkloadStatistics(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<Map<String, Object>> statistics = repairStatisticsService.getTechnicianWorkloadStatistics(startDate, endDate);
        return ApiResponse.success(statistics);
    }

    /**
     * 获取评价统计
     */
    @GetMapping("/evaluation-statistics")
    @Operation(summary = "获取评价统计", description = "获取评价总数、平均评分和评分分布")
    public ApiResponse<Map<String, Object>> getEvaluationStatistics(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        Map<String, Object> statistics = repairStatisticsService.getEvaluationStatistics(startDate, endDate);
        return ApiResponse.success(statistics);
    }

    /**
     * 获取月度工单趋势
     */
    @GetMapping("/monthly-trend")
    @Operation(summary = "获取月度工单趋势", description = "获取最近N个月的工单数量趋势")
    public ApiResponse<List<Map<String, Object>>> getMonthlyOrderTrend(
            @RequestParam(defaultValue = "12") Integer months) {
        List<Map<String, Object>> trend = repairStatisticsService.getMonthlyOrderTrend(months);
        return ApiResponse.success(trend);
    }

    /**
     * 获取设备类型故障率统计
     */
    @GetMapping("/device-failure-rate")
    @Operation(summary = "获取设备类型故障率统计", description = "获取各设备类型的故障率统计")
    public ApiResponse<List<Map<String, Object>>> getDeviceTypeFailureRate(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<Map<String, Object>> statistics = repairStatisticsService.getDeviceTypeFailureRate(startDate, endDate);
        return ApiResponse.success(statistics);
    }
}