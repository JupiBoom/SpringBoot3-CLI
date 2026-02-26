package com.rosy.web.controller.main;

import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.vo.LiveDashboardVO;
import com.rosy.main.service.ILiveStatisticsService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/liveRoom/statistics")
public class LiveStatisticsController {

    @Resource
    private ILiveStatisticsService liveStatisticsService;

    @GetMapping("/dashboard")
    public ApiResponse getDashboard(Long liveRoomId) {
        if (liveRoomId == null || liveRoomId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LiveDashboardVO dashboard = liveStatisticsService.getLiveDashboard(liveRoomId);
        return ApiResponse.success(dashboard);
    }

    @GetMapping("/conversionRate")
    public ApiResponse getConversionRate(Long liveRoomId) {
        if (liveRoomId == null || liveRoomId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        BigDecimal conversionRate = liveStatisticsService.calculateConversionRate(liveRoomId);
        return ApiResponse.success(conversionRate);
    }

    @GetMapping("/retentionCurve")
    public ApiResponse getRetentionCurve(Long liveRoomId) {
        if (liveRoomId == null || liveRoomId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<LiveDashboardVO.ViewerRetentionPoint> curve = liveStatisticsService.getRetentionCurve(liveRoomId);
        return ApiResponse.success(curve);
    }

    @PostMapping("/generate")
    public ApiResponse generateStatistics(Long liveRoomId) {
        if (liveRoomId == null || liveRoomId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        liveStatisticsService.generateHourlyStatistics(liveRoomId);
        return ApiResponse.success(true);
    }
}
