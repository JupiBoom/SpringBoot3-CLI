package com.rosy.web.controller.meeting;

import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.main.domain.dto.MeetingStatisticsRequest;
import com.rosy.main.domain.vo.MeetingStatisticsVO;
import com.rosy.main.service.IMeetingStatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@Tag(name = "会议统计管理")
@RestController
@RequestMapping("/api/meeting/statistics")
public class MeetingStatisticsController {

    @Resource
    private IMeetingStatisticsService meetingStatisticsService;

    @Operation(summary = "获取统计数据")
    @PostMapping("/get")
    public ApiResponse getStatistics(@RequestBody MeetingStatisticsRequest request) {
        MeetingStatisticsVO statistics = meetingStatisticsService.getStatistics(request);
        return ApiResponse.success(statistics);
    }
}
