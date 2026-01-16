package com.rosy.meeting.controller;

import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.meeting.service.INotificationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/meeting/notification")
public class NotificationController {
    private final INotificationService notificationService;

    public NotificationController(INotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/list/{userId}")
    public ApiResponse getNotifications(@PathVariable Long userId) {
        return notificationService.getNotifications(userId);
    }

    @PutMapping("/read/{notificationId}")
    public ApiResponse markAsRead(@PathVariable Long notificationId) {
        return notificationService.markAsRead(notificationId);
    }

    @DeleteMapping("/delete/{notificationId}")
    public ApiResponse deleteNotification(@PathVariable Long notificationId) {
        return notificationService.deleteNotification(notificationId);
    }
}