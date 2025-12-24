package com.rosy.notification.controller;

import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.notification.domain.entity.OrderStatusChangeEvent;
import com.rosy.notification.domain.entity.NotificationRecord;
import com.rosy.notification.domain.entity.UserNotificationPreference;
import com.rosy.notification.domain.entity.NotificationTemplate;
import com.rosy.notification.service.INotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 通知服务控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class NotificationController {

    private final INotificationService notificationService;

    /**
     * 处理订单状态变更事件
     */
    @PostMapping("/order/status/change")
    public ApiResponse<Void> handleOrderStatusChangeEvent(@RequestBody OrderStatusChangeEvent event) {
        try {
            notificationService.handleOrderStatusChangeEvent(event);
            return ApiResponse.success();
        } catch (Exception e) {
            log.error("处理订单状态变更事件失败", e);
            return ApiResponse.error("处理订单状态变更事件失败: " + e.getMessage());
        }
    }

    /**
     * 发送通知
     */
    @PostMapping("/send")
    public ApiResponse<Void> sendNotification(@RequestBody NotificationRecord record) {
        try {
            notificationService.sendNotification(record);
            return ApiResponse.success();
        } catch (Exception e) {
            log.error("发送通知失败", e);
            return ApiResponse.error("发送通知失败: " + e.getMessage());
        }
    }

    /**
     * 获取用户通知偏好设置
     */
    @GetMapping("/user/preferences/{userId}")
    public ApiResponse<List<UserNotificationPreference>> getUserNotificationPreferences(@PathVariable String userId) {
        try {
            List<UserNotificationPreference> preferences = notificationService.getUserNotificationPreferences(userId);
            return ApiResponse.success(preferences);
        } catch (Exception e) {
            log.error("获取用户通知偏好设置失败", e);
            return ApiResponse.error("获取用户通知偏好设置失败: " + e.getMessage());
        }
    }

    /**
     * 获取通知模板
     */
    @GetMapping("/template/{notificationType}/{orderStatus}")
    public ApiResponse<NotificationTemplate> getNotificationTemplate(@PathVariable String notificationType, @PathVariable String orderStatus) {
        try {
            NotificationTemplate template = notificationService.getNotificationTemplate(notificationType, orderStatus);
            return ApiResponse.success(template);
        } catch (Exception e) {
            log.error("获取通知模板失败", e);
            return ApiResponse.error("获取通知模板失败: " + e.getMessage());
        }
    }

    /**
     * 重试发送通知
     */
    @PostMapping("/retry/{notificationId}")
    public ApiResponse<Void> retrySendNotification(@PathVariable String notificationId) {
        try {
            notificationService.retrySendNotification(notificationId);
            return ApiResponse.success();
        } catch (Exception e) {
            log.error("重试发送通知失败", e);
            return ApiResponse.error("重试发送通知失败: " + e.getMessage());
        }
    }
}