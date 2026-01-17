package com.rosy.web.controller.main;

import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.main.domain.entity.Notification;
import com.rosy.main.service.INotificationService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/repair/notification")
public class NotificationController {

    @Resource
    private INotificationService notificationService;

    @PostMapping("/read")
    public ApiResponse markAsRead(@RequestParam Long notificationId) {
        notificationService.markAsRead(notificationId);
        return ApiResponse.success(true);
    }

    @PostMapping("/read/all")
    public ApiResponse markAllAsRead(HttpServletRequest httpRequest) {
        Long userId = (Long) httpRequest.getAttribute("userId");
        notificationService.markAllAsRead(userId);
        return ApiResponse.success(true);
    }

    @GetMapping("/unread")
    public ApiResponse getUnreadNotifications(HttpServletRequest httpRequest) {
        Long userId = (Long) httpRequest.getAttribute("userId");
        List<Notification> notifications = notificationService.getUnreadNotifications(userId);
        return ApiResponse.success(notifications);
    }

    @GetMapping("/all")
    public ApiResponse getAllNotifications(HttpServletRequest httpRequest) {
        Long userId = (Long) httpRequest.getAttribute("userId");
        List<Notification> notifications = notificationService.getAllNotifications(userId);
        return ApiResponse.success(notifications);
    }
}
