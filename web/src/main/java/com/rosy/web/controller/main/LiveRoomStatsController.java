package com.rosy.web.controller.main;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.vo.LiveRoomStatsVO;
import com.rosy.main.service.ILiveRoomStatsService;
import jakarta.annotation.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 直播间数据统计表 前端控制器
 * </p>
 *
 * @author Rosy
 * @since 2025-01-30
 */
@RestController
@RequestMapping("/live-room-stats")
public class LiveRoomStatsController {
    @Resource
    ILiveRoomStatsService liveRoomStatsService;

    /**
     * 获取直播间统计数据列表
     */
    @GetMapping("/list/{liveRoomId}")
    public ApiResponse getStatsByDateRange(
            @PathVariable Long liveRoomId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        if (liveRoomId == null || liveRoomId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        
        // 如果没有指定日期范围，默认查询最近7天
        if (startDate == null) {
            startDate = LocalDate.now().minusDays(7);
        }
        if (endDate == null) {
            endDate = LocalDate.now();
        }
        
        List<LiveRoomStatsVO> statsList = liveRoomStatsService.getStatsByDateRange(liveRoomId, startDate, endDate);
        return ApiResponse.success(statsList);
    }

    /**
     * 分页获取直播间统计数据列表
     */
    @GetMapping("/page/{liveRoomId}")
    public ApiResponse pageStatsByLiveRoomId(
            @PathVariable Long liveRoomId,
            @RequestParam(defaultValue = "1") long current,
            @RequestParam(defaultValue = "10") long size) {
        if (liveRoomId == null || liveRoomId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        
        Page<LiveRoomStatsVO> statsPage = liveRoomStatsService.pageStatsByLiveRoomId(liveRoomId, current, size);
        return ApiResponse.success(statsPage);
    }

    /**
     * 生成每日统计数据
     */
    @PostMapping("/generate-daily")
    @ValidateRequest
    public ApiResponse generateDailyStats(@RequestBody GenerateDailyStatsRequest request) {
        if (request.getLiveRoomId() == null || request.getStatsDate() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        
        boolean result = liveRoomStatsService.generateDailyStats(request.getLiveRoomId(), request.getStatsDate());
        return ApiResponse.success(result);
    }

    /**
     * 更新观众统计数据
     */
    @PostMapping("/update-viewer-stats")
    @ValidateRequest
    public ApiResponse updateViewerStats(@RequestBody UpdateViewerStatsRequest request) {
        if (request.getLiveRoomId() == null || request.getTotalViewers() == null || request.getPeakViewers() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        
        boolean result = liveRoomStatsService.updateViewerStats(
                request.getLiveRoomId(), 
                request.getTotalViewers(), 
                request.getPeakViewers());
        return ApiResponse.success(result);
    }

    /**
     * 更新销售统计数据
     */
    @PostMapping("/update-sales-stats")
    @ValidateRequest
    public ApiResponse updateSalesStats(@RequestBody UpdateSalesStatsRequest request) {
        if (request.getLiveRoomId() == null || request.getTotalOrders() == null || request.getTotalSales() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        
        boolean result = liveRoomStatsService.updateSalesStats(
                request.getLiveRoomId(), 
                request.getTotalOrders(), 
                request.getTotalSales());
        return ApiResponse.success(result);
    }

    /**
     * 生成每日统计数据请求
     */
    public static class GenerateDailyStatsRequest {
        private Long liveRoomId;
        private LocalDate statsDate;

        public Long getLiveRoomId() {
            return liveRoomId;
        }

        public void setLiveRoomId(Long liveRoomId) {
            this.liveRoomId = liveRoomId;
        }

        public LocalDate getStatsDate() {
            return statsDate;
        }

        public void setStatsDate(LocalDate statsDate) {
            this.statsDate = statsDate;
        }
    }

    /**
     * 更新观众统计数据请求
     */
    public static class UpdateViewerStatsRequest {
        private Long liveRoomId;
        private Integer totalViewers;
        private Integer peakViewers;

        public Long getLiveRoomId() {
            return liveRoomId;
        }

        public void setLiveRoomId(Long liveRoomId) {
            this.liveRoomId = liveRoomId;
        }

        public Integer getTotalViewers() {
            return totalViewers;
        }

        public void setTotalViewers(Integer totalViewers) {
            this.totalViewers = totalViewers;
        }

        public Integer getPeakViewers() {
            return peakViewers;
        }

        public void setPeakViewers(Integer peakViewers) {
            this.peakViewers = peakViewers;
        }
    }

    /**
     * 更新销售统计数据请求
     */
    public static class UpdateSalesStatsRequest {
        private Long liveRoomId;
        private Integer totalOrders;
        private java.math.BigDecimal totalSales;

        public Long getLiveRoomId() {
            return liveRoomId;
        }

        public void setLiveRoomId(Long liveRoomId) {
            this.liveRoomId = liveRoomId;
        }

        public Integer getTotalOrders() {
            return totalOrders;
        }

        public void setTotalOrders(Integer totalOrders) {
            this.totalOrders = totalOrders;
        }

        public java.math.BigDecimal getTotalSales() {
            return totalSales;
        }

        public void setTotalSales(java.math.BigDecimal totalSales) {
            this.totalSales = totalSales;
        }
    }
}