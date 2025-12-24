package com.rosy.web.controller.main;

import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.main.domain.entity.UserNotificationPreference;
import com.rosy.main.service.IUserNotificationPreferenceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;

/**
 * 用户通知偏好设置控制器
 *
 * @author rosy
 */
@RestController
@RequestMapping("/userNotificationPreference")
@Tag(name = "用户通知偏好设置", description = "用户通知偏好设置相关接口")
public class UserNotificationPreferenceController {

    @Resource
    private IUserNotificationPreferenceService userNotificationPreferenceService;

    @GetMapping("/get/{userId}")
    @Operation(summary = "获取用户通知偏好设置", description = "根据用户ID获取用户通知偏好设置")
    public ApiResponse getUserPreference(@PathVariable Long userId) {
        UserNotificationPreference preference = userNotificationPreferenceService.getUserPreference(userId);
        return ApiResponse.success(preference);
    }

    @PutMapping("/update")
    @Operation(summary = "更新用户通知偏好设置", description = "更新用户通知偏好设置")
    public ApiResponse updateUserPreference(@RequestBody UserNotificationPreference preference) {
        boolean success = userNotificationPreferenceService.updateUserPreference(preference);
        return ApiResponse.success(success);
    }
}