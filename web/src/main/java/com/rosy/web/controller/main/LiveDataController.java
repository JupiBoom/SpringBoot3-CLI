package com.rosy.web.controller.main;

import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.main.domain.vo.LiveDataVO;
import com.rosy.main.domain.vo.LiveGoodsRankVO;
import com.rosy.main.service.ILiveDataService;
import com.rosy.main.service.ILiveGoodsRankService;
import com.rosy.main.service.ILiveUserRetentionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 直播间数据统计接口
 * </p>
 *
 * @author Rosy
 * @since 2026-01-16
 */
@RestController
@RequestMapping("/live-data")
@Tag(name = "直播间数据统计接口")
public class LiveDataController {

    private final ILiveDataService liveDataService;
    private final ILiveGoodsRankService liveGoodsRankService;
    private final ILiveUserRetentionService liveUserRetentionService;

    public LiveDataController(
            ILiveDataService liveDataService,
            ILiveGoodsRankService liveGoodsRankService,
            ILiveUserRetentionService liveUserRetentionService) {
        this.liveDataService = liveDataService;
        this.liveGoodsRankService = liveGoodsRankService;
        this.liveUserRetentionService = liveUserRetentionService;
    }

    @GetMapping("/{roomId}")
    @Operation(summary = "获取直播间数据")
    public ApiResponse getLiveData(@PathVariable Long roomId) {
        LiveDataVO liveDataVO = liveDataService.getLiveData(roomId);
        return ApiResponse.success(liveDataVO);
    }

    @GetMapping("/{roomId}/rank")
    @Operation(summary = "获取商品排行榜")
    public ApiResponse getRankList(@PathVariable Long roomId) {
        List<LiveGoodsRankVO> rankList = liveGoodsRankService.getRankList(roomId);
        return ApiResponse.success(rankList);
    }

    @GetMapping("/{roomId}/retention")
    @Operation(summary = "获取观众留存曲线")
    public ApiResponse getRetentionCurve(@PathVariable Long roomId) {
        Map<LocalDateTime, Integer> retentionCurve = liveUserRetentionService.getRetentionCurve(roomId);
        return ApiResponse.success(retentionCurve);
    }

    @GetMapping("/{roomId}/average-duration")
    @Operation(summary = "获取平均停留时长")
    public ApiResponse getAverageDuration(@PathVariable Long roomId) {
        Double averageDuration = liveUserRetentionService.getAverageDuration(roomId);
        return ApiResponse.success(averageDuration);
    }
}
