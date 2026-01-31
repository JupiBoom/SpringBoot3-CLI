package com.rosy.web.controller.main;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.main.domain.dto.RegistrationDTO;
import com.rosy.main.domain.dto.RegistrationReviewDTO;
import com.rosy.main.domain.vo.RegistrationVO;
import com.rosy.main.service.IRegistrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "报名管理")
@RestController
@RequestMapping("/api/registration")
@RequiredArgsConstructor
public class RegistrationController {

    private final IRegistrationService registrationService;

    @Operation(summary = "报名活动")
    @PostMapping
    public ApiResponse createRegistration(@Validated @RequestBody RegistrationDTO dto) {
        Long userId = 1L;
        return ApiResponse.success(registrationService.createRegistration(dto, userId));
    }

    @Operation(summary = "审核报名（人工）")
    @PostMapping("/review")
    public ApiResponse reviewRegistration(@Validated @RequestBody RegistrationReviewDTO dto) {
        Long reviewerId = 1L;
        registrationService.manualReviewRegistration(dto, reviewerId);
        return ApiResponse.success();
    }

    @Operation(summary = "获取报名详情")
    @GetMapping("/{registrationId}")
    public ApiResponse getRegistrationDetail(
            @Parameter(description = "报名ID") @PathVariable Long registrationId) {
        return ApiResponse.success(registrationService.getRegistrationDetail(registrationId));
    }

    @Operation(summary = "获取活动的报名列表")
    @GetMapping("/activity/{activityId}")
    public ApiResponse getRegistrationByActivity(
            @Parameter(description = "活动ID") @PathVariable Long activityId,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer pageSize) {
        return ApiResponse.success(registrationService.getRegistrationByActivity(activityId, pageNum, pageSize));
    }

    @Operation(summary = "获取我的报名列表")
    @GetMapping("/my")
    public ApiResponse getMyRegistration(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer pageSize) {
        Long userId = 1L;
        return ApiResponse.success(registrationService.getRegistrationByUser(userId, pageNum, pageSize));
    }

    @Operation(summary = "取消报名")
    @DeleteMapping("/{registrationId}")
    public ApiResponse cancelRegistration(
            @Parameter(description = "报名ID") @PathVariable Long registrationId) {
        Long userId = 1L;
        registrationService.cancelRegistration(registrationId, userId);
        return ApiResponse.success();
    }
}
