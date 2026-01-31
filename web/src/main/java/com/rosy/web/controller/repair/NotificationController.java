package com.rosy.web.controller.repair;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.entity.Notification;
import com.rosy.main.service.INotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 通知表 前端控制器
 * </p>
 *
 * @author Rosy
 * @since 2025-01-31
 */
@RestController
@RequestMapping("/repair/notification")
@Tag(name = "通知管理", description = "通知相关接口")
public class NotificationController {

    @Resource
    private INotificationService notificationService;

    /**
     * 标记通知为已读
     */
    @PostMapping("/mark-read")
    @Operation(summary = "标记通知为已读", description = "用户标记通知为已读状态")
    public ApiResponse<Boolean> markAsRead(@RequestParam Long notificationId, HttpServletRequest request) {
        // TODO: 从请求中获取当前登录用户ID
        Long userId = 1L; // 临时硬编码，实际应从登录信息中获取
        
        boolean result = notificationService.markAsRead(notificationId, userId);
        return ApiResponse.success(result);
    }

    /**
     * 批量标记通知为已读
     */
    @PostMapping("/mark-read-batch")
    @Operation(summary = "批量标记通知为已读", description = "用户批量标记通知为已读状态")
    public ApiResponse<Boolean> markAsReadBatch(@RequestBody List<Long> notificationIds, HttpServletRequest request) {
        // TODO: 从请求中获取当前登录用户ID
        Long userId = 1L; // 临时硬编码，实际应从登录信息中获取
        
        boolean result = notificationService.markAsReadBatch(notificationIds, userId);
        return ApiResponse.success(result);
    }

    /**
     * 获取用户未读通知数量
     */
    @GetMapping("/unread-count")
    @Operation(summary = "获取未读通知数量", description = "获取当前用户的未读通知数量")
    public ApiResponse<Long> getUnreadCount(HttpServletRequest request) {
        // TODO: 从请求中获取当前登录用户ID
        Long userId = 1L; // 临时硬编码，实际应从登录信息中获取
        
        Long count = notificationService.getUnreadCount(userId);
        return ApiResponse.success(count);
    }

    /**
     * 分页获取用户通知列表
     */
    @GetMapping("/list")
    @Operation(summary = "获取通知列表", description = "分页获取用户的通知列表")
    public ApiResponse<Page<Notification>> pageUserNotifications(
            @RequestParam(defaultValue = "1") long current,
            @RequestParam(defaultValue = "10") long size,
            HttpServletRequest request) {
        // TODO: 从请求中获取当前登录用户ID
        Long userId = 1L; // 临时硬编码，实际应从登录信息中获取
        
        Page<Notification> notificationPage = notificationService.pageUserNotifications(userId, current, size);
        return ApiResponse.success(notificationPage);
    }
}