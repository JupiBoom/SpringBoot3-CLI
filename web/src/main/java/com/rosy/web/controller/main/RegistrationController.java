package com.rosy.web.controller.main;

import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.main.domain.dto.RegistrationAuditDTO;
import com.rosy.main.domain.entity.Registration;
import com.rosy.main.service.IRegistrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/registration")
@Tag(name = "报名管理")
public class RegistrationController {

    @Resource
    private IRegistrationService registrationService;

    @PostMapping("/apply")
    @ValidateRequest
    @Operation(summary = "报名活动")
    public ApiResponse apply(@RequestBody ApplyRequest request) {
        Registration registration = registrationService.register(request.getActivityId(), request.getUserId());
        return ApiResponse.success(registration);
    }

    @PostMapping("/audit")
    @ValidateRequest
    @Operation(summary = "审核报名")
    public ApiResponse audit(@RequestBody RegistrationAuditDTO dto) {
        registrationService.audit(dto, 1L);
        return ApiResponse.success(true);
    }

    @PostMapping("/cancel")
    @ValidateRequest
    @Operation(summary = "取消报名")
    public ApiResponse cancel(@RequestBody IdRequest idRequest) {
        registrationService.cancel(idRequest.getId(), 1L);
        return ApiResponse.success(true);
    }

    @GetMapping("/my")
    @Operation(summary = "获取我的报名")
    public ApiResponse getMyRegistrations(Long userId) {
        List<Registration> registrations = registrationService.getMyRegistrations(userId);
        return ApiResponse.success(registrations);
    }

    @GetMapping("/activity")
    @Operation(summary = "获取活动报名列表")
    public ApiResponse getActivityRegistrations(Long activityId) {
        List<Registration> registrations = registrationService.getActivityRegistrations(activityId);
        return ApiResponse.success(registrations);
    }

    @Data
    public static class ApplyRequest {
        private Long activityId;
        private Long userId;
    }
}
