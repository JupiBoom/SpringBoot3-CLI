package com.rosy.web.controller.main;

import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.vo.LiveConversionRateVO;
import com.rosy.main.domain.vo.ViewerRetentionCurveVO;
import com.rosy.main.service.ILiveRoomAnalyticsService;
import jakarta.annotation.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 直播间数据分析前端控制器
 * </p>
 *
 * @author Rosy
 * @since 2025-01-30
 */
@RestController
@RequestMapping("/live-room-analytics")
public class LiveRoomAnalyticsController {
    @Resource
    ILiveRoomAnalyticsService liveRoomAnalyticsService;

    /**
     * 获取直播间转化率统计
     */
    @GetMapping("/conversion-rate/{liveRoomId}")
    public ApiResponse getConversionRateStats(
            @PathVariable Long liveRoomId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        if (liveRoomId == null || liveRoomId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        
        LiveConversionRateVO conversionRateVO = liveRoomAnalyticsService.getConversionRateStats(liveRoomId, startDate, endDate);
        return ApiResponse.success(conversionRateVO);
    }

    /**
     * 获取观众留存曲线
     */
    @GetMapping("/viewer-retention/{liveRoomId}")
    public ApiResponse<ViewerRetentionCurveVO> getViewerRetentionCurve(@PathVariable Long liveRoomId) {
        if (liveRoomId == null || liveRoomId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        
        ViewerRetentionCurveVO retentionCurve = liveRoomAnalyticsService.getViewerRetentionCurve(liveRoomId);
        return ApiResponse.success(retentionCurve);
    }

    /**
     * 获取多个直播间的转化率对比
     */
    @PostMapping("/conversion-rate-comparison")
    @ValidateRequest
    public ApiResponse<List<LiveConversionRateVO>> getConversionRateComparison(@RequestBody ConversionRateComparisonRequest request) {
        if (request.getLiveRoomIds() == null || request.getLiveRoomIds().isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        
        List<LiveConversionRateVO> comparison = liveRoomAnalyticsService.getConversionRateComparison(
                request.getLiveRoomIds(), 
                request.getStartDate(), 
                request.getEndDate());
        return ApiResponse.success(comparison);
    }

    /**
     * 获取观众留存曲线对比
     */
    @PostMapping("/viewer-retention-comparison")
    @ValidateRequest
    public ApiResponse<List<ViewerRetentionCurveVO>> getViewerRetentionComparison(@RequestBody ViewerRetentionComparisonRequest request) {
        if (request.getLiveRoomIds() == null || request.getLiveRoomIds().isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        
        List<ViewerRetentionCurveVO> comparison = liveRoomAnalyticsService.getViewerRetentionComparison(request.getLiveRoomIds());
        return ApiResponse.success(comparison);
    }

    /**
     * 转化率对比请求
     */
    public static class ConversionRateComparisonRequest {
        private List<Long> liveRoomIds;
        private LocalDate startDate;
        private LocalDate endDate;

        public List<Long> getLiveRoomIds() {
            return liveRoomIds;
        }

        public void setLiveRoomIds(List<Long> liveRoomIds) {
            this.liveRoomIds = liveRoomIds;
        }

        public LocalDate getStartDate() {
            return startDate;
        }

        public void setStartDate(LocalDate startDate) {
            this.startDate = startDate;
        }

        public LocalDate getEndDate() {
            return endDate;
        }

        public void setEndDate(LocalDate endDate) {
            this.endDate = endDate;
        }
    }

    /**
     * 观众留存曲线对比请求
     */
    public static class ViewerRetentionComparisonRequest {
        private List<Long> liveRoomIds;

        public List<Long> getLiveRoomIds() {
            return liveRoomIds;
        }

        public void setLiveRoomIds(List<Long> liveRoomIds) {
            this.liveRoomIds = liveRoomIds;
        }
    }
}