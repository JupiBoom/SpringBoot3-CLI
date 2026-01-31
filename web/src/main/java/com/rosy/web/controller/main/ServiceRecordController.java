package com.rosy.web.controller.main;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.main.domain.dto.ReviewDTO;
import com.rosy.main.domain.vo.ReviewVO;
import com.rosy.main.domain.vo.ServiceRecordVO;
import com.rosy.main.service.IServiceRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "服务记录")
@RestController
@RequestMapping("/api/service")
@RequiredArgsConstructor
public class ServiceRecordController {

    private final IServiceRecordService serviceRecordService;

    @Operation(summary = "签到")
    @PostMapping("/checkin/{registrationId}")
    public ApiResponse checkIn(
            @Parameter(description = "报名ID") @PathVariable Long registrationId) {
        Long userId = 1L;
        serviceRecordService.checkIn(registrationId, userId);
        return ApiResponse.success();
    }

    @Operation(summary = "签出")
    @PostMapping("/checkout/{registrationId}")
    public ApiResponse checkOut(
            @Parameter(description = "报名ID") @PathVariable Long registrationId) {
        Long userId = 1L;
        serviceRecordService.checkOut(registrationId, userId);
        return ApiResponse.success();
    }

    @Operation(summary = "获取服务记录详情")
    @GetMapping("/{recordId}")
    public ApiResponse getServiceRecordDetail(
            @Parameter(description = "记录ID") @PathVariable Long recordId) {
        return ApiResponse.success(serviceRecordService.getServiceRecordDetail(recordId));
    }

    @Operation(summary = "获取我的服务记录")
    @GetMapping("/my")
    public ApiResponse getMyServiceRecords(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer pageSize) {
        Long userId = 1L;
        return ApiResponse.success(serviceRecordService.getServiceRecordsByUser(userId, pageNum, pageSize));
    }

    @Operation(summary = "获取活动的服务记录")
    @GetMapping("/activity/{activityId}")
    public ApiResponse getServiceRecordsByActivity(
            @Parameter(description = "活动ID") @PathVariable Long activityId,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer pageSize) {
        return ApiResponse.success(serviceRecordService.getServiceRecordsByActivity(activityId, pageNum, pageSize));
    }

    @Operation(summary = "创建评价")
    @PostMapping("/review")
    public ApiResponse createReview(@Validated @RequestBody ReviewDTO dto) {
        Long userId = 1L;
        return ApiResponse.success(serviceRecordService.createReview(dto, userId));
    }

    @Operation(summary = "获取活动评价列表")
    @GetMapping("/review/activity/{activityId}")
    public ApiResponse getReviewsByActivity(
            @Parameter(description = "活动ID") @PathVariable Long activityId,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer pageSize) {
        return ApiResponse.success(serviceRecordService.getReviewsByActivity(activityId, pageNum, pageSize));
    }

    @Operation(summary = "生成服务证明")
    @GetMapping("/certificate/{recordId}")
    public ApiResponse generateServiceCertificate(
            @Parameter(description = "记录ID") @PathVariable Long recordId) {
        Long userId = 1L;
        return ApiResponse.success(serviceRecordService.generateServiceCertificate(recordId, userId));
    }

    @Operation(summary = "获取总服务时长")
    @GetMapping("/total-hours")
    public ApiResponse getTotalServiceHours() {
        Long userId = 1L;
        return ApiResponse.success(serviceRecordService.calculateTotalServiceHours(userId));
    }
}
