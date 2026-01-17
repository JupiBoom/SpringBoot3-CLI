package com.rosy.web.controller.main;

import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.main.domain.vo.repair.StatisticsVO;
import com.rosy.main.service.IStatisticsService;
import jakarta.annotation.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/repair/statistics")
public class StatisticsController {

    @Resource
    private IStatisticsService statisticsService;

    @GetMapping("/get")
    public ApiResponse getStatistics(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        StatisticsVO vo = statisticsService.getStatistics(startTime, endTime);
        return ApiResponse.success(vo);
    }
}
