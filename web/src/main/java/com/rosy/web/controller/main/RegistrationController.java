package com.rosy.web.controller.main;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.PageUtils;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.entity.Registration;
import com.rosy.main.service.IRegistrationService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/registration")
public class RegistrationController {

    @Resource
    private IRegistrationService registrationService;

    @PostMapping("/register")
    @ValidateRequest
    public ApiResponse registerActivity(@RequestParam Long activityId, @RequestParam Long userId, @RequestParam(defaultValue = "1") byte auditType) {
        boolean result = registrationService.registerActivity(activityId, userId, auditType);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(true);
    }

    @PostMapping("/audit")
    @ValidateRequest
    public ApiResponse auditRegistration(@RequestParam Long registrationId, @RequestParam Long auditorId, @RequestParam byte status, @RequestParam(required = false) String remark) {
        boolean result = registrationService.auditRegistration(registrationId, auditorId, status, remark);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(true);
    }

    @GetMapping("/list")
    public ApiResponse listRegistrations(@RequestParam(required = false) Long activityId, @RequestParam(required = false) Long userId, @RequestParam(required = false) Byte status) {
        var queryWrapper = registrationService.getQueryWrapper(activityId, userId, status);
        return ApiResponse.success(registrationService.list(queryWrapper));
    }

    @GetMapping("/list/page")
    public ApiResponse listRegistrationsByPage(@RequestParam(required = false) Long activityId, @RequestParam(required = false) Long userId, @RequestParam(required = false) Byte status, @RequestParam(defaultValue = "1") long current, @RequestParam(defaultValue = "10") long size) {
        var queryWrapper = registrationService.getQueryWrapper(activityId, userId, status);
        Page<Registration> page = registrationService.page(new Page<>(current, size), queryWrapper);
        return ApiResponse.success(page);
    }

    @GetMapping("/check")
    public ApiResponse checkDuplicateRegistration(@RequestParam Long activityId, @RequestParam Long userId) {
        boolean isDuplicate = registrationService.checkDuplicateRegistration(activityId, userId);
        return ApiResponse.success(isDuplicate);
    }
}
