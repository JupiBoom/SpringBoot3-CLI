package com.rosy.web.controller.main;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.PageRequest;
import com.rosy.main.domain.entity.Notification;
import com.rosy.main.enums.NotificationTypeEnum;
import com.rosy.main.service.INotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 通知管理控制器
 */
@Tag(name = "通知管理", description = "通知相关接口")
@RestController
@RequestMapping("/notification")
public class NotificationController {

    @Autowired
    private INotificationService notificationService;

    @Operation(summary = "发送通知")
    @PostMapping
    public ApiResponse<Boolean> sendNotification(@RequestParam Long userId,
                                               @RequestParam String title,
                                               @RequestParam String content,
                                               @RequestParam Integer type,
                                               @RequestParam(required = false) Long relatedId) {
        boolean result = notificationService.sendNotification(userId, title, content, type, relatedId);
        if (result) {
            return ApiResponse.success(true);
        } else {
            return ApiResponse.error("发送通知失败");
        }
    }

    @Operation(summary = "批量发送通知")
    @PostMapping("/batch")
    public ApiResponse<Boolean> sendNotificationBatch(@RequestBody List<Long> userIds,
                                                     @RequestParam String title,
                                                     @RequestParam String content,
                                                     @RequestParam Integer type,
                                                     @RequestParam(required = false) Long relatedId) {
        boolean result = notificationService.sendNotificationBatch(userIds, title, content, type, relatedId);
        if (result) {
            return ApiResponse.success(true);
        } else {
            return ApiResponse.error("批量发送通知失败");
        }
    }

    @Operation(summary = "根据ID获取通知详情")
    @GetMapping("/{id}")
    public ApiResponse<Notification> getNotificationById(@Parameter(description = "通知ID") @PathVariable Long id) {
        Notification notification = notificationService.getById(id);
        if (notification != null) {
            return ApiResponse.success(notification);
        } else {
            return ApiResponse.error("通知不存在");
        }
    }

    @Operation(summary = "分页查询通知")
    @GetMapping("/page")
    public ApiResponse<Page<Notification>> getNotificationPage(PageRequest pageRequest,
                                                              @Parameter(description = "用户ID") @RequestParam(required = false) Long userId,
                                                              @Parameter(description = "通知类型") @RequestParam(required = false) Integer type,
                                                              @Parameter(description = "是否已读") @RequestParam(required = false) Integer isRead) {
        Page<Notification> page = new Page<>(pageRequest.getCurrent(), pageRequest.getSize());
        Page<Notification> result = notificationService.getNotificationPage(page, userId, type, isRead);
        return ApiResponse.success(result);
    }

    @Operation(summary = "标记通知为已读")
    @PutMapping("/{id}/read")
    public ApiResponse<Boolean> markAsRead(@Parameter(description = "通知ID") @PathVariable Long id) {
        boolean result = notificationService.markAsRead(id);
        if (result) {
            return ApiResponse.success(true);
        } else {
            return ApiResponse.error("标记已读失败");
        }
    }

    @Operation(summary = "批量标记通知为已读")
    @PutMapping("/batch/read")
    public ApiResponse<Boolean> markAsReadBatch(@RequestBody List<Long> notificationIds) {
        boolean result = notificationService.markAsReadBatch(notificationIds);
        if (result) {
            return ApiResponse.success(true);
        } else {
            return ApiResponse.error("批量标记已读失败");
        }
    }

    @Operation(summary = "标记用户所有通知为已读")
    @PutMapping("/user/{userId}/read-all")
    public ApiResponse<Boolean> markAllAsRead(@Parameter(description = "用户ID") @PathVariable Long userId) {
        boolean result = notificationService.markAllAsRead(userId);
        if (result) {
            return ApiResponse.success(true);
        } else {
            return ApiResponse.error("标记全部已读失败");
        }
    }

    @Operation(summary = "获取用户未读通知数量")
    @GetMapping("/unread-count")
    public ApiResponse<Long> getUnreadCount(@RequestParam Long userId) {
        long count = notificationService.getUnreadCount(userId);
        return ApiResponse.success(count);
    }

    @Operation(summary = "获取通知类型列表")
    @GetMapping("/types")
    public ApiResponse<List<NotificationTypeEnum>> getNotificationTypes() {
        return ApiResponse.success(List.of(NotificationTypeEnum.values()));
    }
}