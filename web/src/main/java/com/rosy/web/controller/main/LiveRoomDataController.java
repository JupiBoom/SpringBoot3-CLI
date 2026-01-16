package com.rosy.web.controller.main;

import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.entity.LiveRoomData;
import com.rosy.main.domain.vo.LiveRoomAnalyticsVO;
import com.rosy.main.domain.vo.LiveRoomDataVO;
import com.rosy.main.domain.vo.ProductRankingVO;
import com.rosy.main.domain.vo.ViewerRetentionVO;
import com.rosy.main.service.ILiveRoomDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/liveRoomData")
@Tag(name = "直播间数据统计", description = "直播间数据统计相关接口")
public class LiveRoomDataController {

    @Resource
    private ILiveRoomDataService liveRoomDataService;

    @PostMapping("/record")
    @Operation(summary = "记录直播间数据")
    @ValidateRequest
    public ApiResponse recordLiveRoomData(@RequestBody LiveRoomData liveRoomData) {
        ThrowUtils.throwIf(liveRoomData == null, ErrorCode.PARAMS_ERROR);
        LiveRoomDataVO result = liveRoomDataService.recordLiveRoomData(liveRoomData);
        return ApiResponse.success(result);
    }

    @GetMapping("/list")
    @Operation(summary = "获取直播间数据列表")
    public ApiResponse getLiveRoomDataList(@RequestParam Long liveRoomId) {
        ThrowUtils.throwIf(liveRoomId == null || liveRoomId <= 0, ErrorCode.PARAMS_ERROR);
        List<LiveRoomDataVO> result = liveRoomDataService.getLiveRoomDataList(liveRoomId);
        return ApiResponse.success(result);
    }

    @GetMapping("/latest")
    @Operation(summary = "获取最新直播间数据")
    public ApiResponse getLatestData(@RequestParam Long liveRoomId) {
        ThrowUtils.throwIf(liveRoomId == null || liveRoomId <= 0, ErrorCode.PARAMS_ERROR);
        LiveRoomDataVO result = liveRoomDataService.getLatestData(liveRoomId);
        return ApiResponse.success(result);
    }

    @GetMapping("/analytics")
    @Operation(summary = "获取直播间数据分析")
    public ApiResponse getAnalytics(@RequestParam Long liveRoomId) {
        ThrowUtils.throwIf(liveRoomId == null || liveRoomId <= 0, ErrorCode.PARAMS_ERROR);
        LiveRoomAnalyticsVO result = liveRoomDataService.getAnalytics(liveRoomId);
        return ApiResponse.success(result);
    }

    @GetMapping("/productRanking")
    @Operation(summary = "获取商品排行榜")
    public ApiResponse getProductRanking(@RequestParam Long liveRoomId) {
        ThrowUtils.throwIf(liveRoomId == null || liveRoomId <= 0, ErrorCode.PARAMS_ERROR);
        List<ProductRankingVO> result = liveRoomDataService.getProductRanking(liveRoomId);
        return ApiResponse.success(result);
    }

    @GetMapping("/viewerRetention")
    @Operation(summary = "获取观众留存曲线")
    public ApiResponse getViewerRetention(@RequestParam Long liveRoomId) {
        ThrowUtils.throwIf(liveRoomId == null || liveRoomId <= 0, ErrorCode.PARAMS_ERROR);
        ViewerRetentionVO result = liveRoomDataService.getViewerRetention(liveRoomId);
        return ApiResponse.success(result);
    }
}