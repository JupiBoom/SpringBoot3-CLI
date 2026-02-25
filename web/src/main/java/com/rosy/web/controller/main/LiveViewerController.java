package com.rosy.web.controller.main;

import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.dto.live.LiveViewerEnterRequest;
import com.rosy.main.domain.dto.live.LiveViewerLeaveRequest;
import com.rosy.main.service.ILiveViewerStatsService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 直播间观众管理 前端控制器
 * </p>
 *
 * @author Rosy
 * @since 2025-02-25
 */
@RestController
@RequestMapping("/live/viewer")
public class LiveViewerController {

    @Resource
    private ILiveViewerStatsService liveViewerStatsService;

    /**
     * 观众进入直播间
     */
    @PostMapping("/enter")
    @ValidateRequest
    public ApiResponse viewerEnter(@RequestBody LiveViewerEnterRequest request) {
        Long statsId = liveViewerStatsService.viewerEnter(request);
        return ApiResponse.success(statsId);
    }

    /**
     * 观众离开直播间
     */
    @PostMapping("/leave")
    @ValidateRequest
    public ApiResponse viewerLeave(@RequestBody LiveViewerLeaveRequest request) {
        boolean result = liveViewerStatsService.viewerLeave(request);
        return ApiResponse.success(result);
    }

    /**
     * 获取直播间累计观看人数
     */
    @GetMapping("/total")
    public ApiResponse getTotalViewers(long roomId) {
        if (roomId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long count = liveViewerStatsService.getTotalViewers(roomId);
        return ApiResponse.success(count);
    }

    /**
     * 获取直播间平均停留时长（秒）
     */
    @GetMapping("/avgStay")
    public ApiResponse getAvgStayDuration(long roomId) {
        if (roomId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Double duration = liveViewerStatsService.getAvgStayDuration(roomId);
        return ApiResponse.success(duration);
    }

    /**
     * 获取观众停留时长分布
     */
    @GetMapping("/stayDistribution")
    public ApiResponse getStayDurationDistribution(long roomId) {
        if (roomId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Map<String, Object> distribution = liveViewerStatsService.getStayDurationDistribution(roomId);
        return ApiResponse.success(distribution);
    }

    /**
     * 获取观众留存曲线数据
     */
    @GetMapping("/retention")
    public ApiResponse getRetentionCurve(long roomId) {
        if (roomId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<Map<String, Object>> curve = liveViewerStatsService.generateRetentionCurve(roomId);
        return ApiResponse.success(curve);
    }
}
