package com.rosy.web.controller.main;

import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.main.domain.entity.Notification;
import com.rosy.main.service.INotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notification")
@Tag(name = "通知管理")
public class NotificationController {

    @Resource
    private INotificationService notificationService;

    @GetMapping("/my")
    @Operation(summary = "获取我的通知")
    public ApiResponse getMyNotifications(Long userId) {
        List<Notification> notifications = notificationService.getMyNotifications(userId);
        return ApiResponse.success(notifications);
    }

    @GetMapping("/unread/count")
    @Operation(summary = "获取未读通知数量")
    public ApiResponse countUnread(Long userId) {
        int count = notificationService.countUnread(userId);
        return ApiResponse.success(count);
    }

    @PostMapping("/read")
    @ValidateRequest
    @Operation(summary = "标记为已读")
    public ApiResponse markAsRead(@RequestBody IdRequest idRequest) {
        notificationService.markAsRead(idRequest.getId(), 1L);
        return ApiResponse.success(true);
    }
}
