package com.rosy.web.controller.main;

import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.main.domain.vo.LiveRoomConversionRateVO;
import com.rosy.main.domain.vo.LiveRoomRetentionVO;
import com.rosy.main.service.ILiveRoomAnalysisService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/live/room/analysis")
public class LiveRoomAnalysisController {

    @Resource
    ILiveRoomAnalysisService liveRoomAnalysisService;

    @GetMapping("/conversion-rate/{liveRoomId}")
    public ApiResponse getConversionRate(@PathVariable Long liveRoomId) {
        LiveRoomConversionRateVO vo = liveRoomAnalysisService.calculateConversionRate(liveRoomId);
        return ApiResponse.success(vo);
    }

    @GetMapping("/retention-curve/{liveRoomId}")
    public ApiResponse getRetentionCurve(@PathVariable Long liveRoomId) {
        LiveRoomRetentionVO vo = liveRoomAnalysisService.getAudienceRetentionCurve(liveRoomId);
        return ApiResponse.success(vo);
    }
}