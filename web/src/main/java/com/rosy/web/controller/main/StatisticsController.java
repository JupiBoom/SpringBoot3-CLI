package com.rosy.web.controller.main;

import com.rosy.common.core.domain.R;
import com.rosy.main.domain.vo.DashboardStatisticsVO;
import com.rosy.main.service.IRepairOrderService;
import org.springframework.transaction.annotation.Transactional;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 统计分析控制器
 */
@RestController
@RequestMapping("/api/repair/statistics")
@Tag(name = "统计分析")
public class StatisticsController {

    @Autowired
    private IRepairOrderService repairOrderService;

    /**
     * 获取仪表板统计数据
     */
    @GetMapping("/dashboard")
    @Operation(summary = "获取仪表板统计数据")
    public R<DashboardStatisticsVO> getDashboardStatistics(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        DashboardStatisticsVO statistics = repairOrderService.getDashboardStatistics(startDate, endDate);
        return R.ok(statistics);
    }

    /**
     * 获取平均响应时间（小时）
     */
    @GetMapping("/avg-response-time")
    @Operation(summary = "获取平均响应时间")
    public R<Map<String, Object>> getAverageResponseTime(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        Double avgHours = repairOrderService.getAverageResponseTime(startDate, endDate);
        return R.ok(Map.of(
            "avgResponseTimeHours", avgHours,
            "avgResponseTimeMinutes", avgHours != null ? avgHours * 60 : null
        ));
    }

    /**
     * 获取故障类型统计
     */
    @GetMapping("/fault-type-statistics")
    @Operation(summary = "获取故障类型统计")
    public R<Map<String, Integer>> getFaultTypeStatistics(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        Map<String, Integer> statistics = repairOrderService.getFaultTypeStatistics(startDate, endDate);
        return R.ok(statistics);
    }

    /**
     * 获取工单趋势统计
     */
    @GetMapping("/order-trend")
    @Operation(summary = "获取工单趋势统计")
    public R<Map<String, Map<String, Integer>>> getOrderTrendStatistics(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(defaultValue = "day") String groupBy) {
        Map<String, Map<String, Integer>> statistics = repairOrderService.getOrderTrendStatistics(
            startDate, endDate, groupBy);
        return R.ok(statistics);
    }

    /**
     * 获取工单状态统计
     */
    @GetMapping("/status-statistics")
    @Operation(summary = "获取工单状态统计")
    public R<Map<String, Integer>> getStatusStatistics(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        Map<String, Integer> statistics = repairOrderService.getStatusStatistics(startDate, endDate);
        return R.ok(statistics);
    }

    /**
     * 获取设备类型故障统计
     */
    @GetMapping("/equipment-type-statistics")
    @Operation(summary = "获取设备类型故障统计")
    public R<Map<String, Integer>> getEquipmentTypeStatistics(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        Map<String, Integer> statistics = repairOrderService.getEquipmentTypeStatistics(startDate, endDate);
        return R.ok(statistics);
    }

    /**
     * 获取工单数量统计（按天/周/月）
     */
    @GetMapping("/order-count")
    @Operation(summary = "获取工单数量统计")
    public R<Map<String, Object>> getOrderCountStatistics(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        DashboardStatisticsVO statistics = repairOrderService.getDashboardStatistics(startDate, endDate);
        
        Map<String, Object> result = new java.util.HashMap<>();
        result.put("totalOrders", statistics.getTotalOrders());
        result.put("pendingOrders", statistics.getPendingOrders());
        result.put("processingOrders", statistics.getProcessingOrders());
        result.put("completedOrders", statistics.getCompletedOrders());
        result.put("avgResponseTime", statistics.getAvgResponseTime());
        
        return R.ok(result);
    }
}