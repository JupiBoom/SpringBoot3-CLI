package com.rosy.notification.service;

import com.rosy.notification.domain.entity.OrderStatusChangeEvent;
import com.rosy.notification.domain.entity.NotificationRecord;
import com.rosy.notification.domain.entity.UserNotificationPreference;
import com.rosy.notification.domain.entity.NotificationTemplate;
import java.util.List;
import java.util.Map;

/**
 * 通知服务接口
 */
public interface INotificationService {
    /**
     * 处理订单状态变更事件
     */
    void handleOrderStatusChangeEvent(OrderStatusChangeEvent event);

    /**
     * 发送通知
     */
    void sendNotification(NotificationRecord record);

    /**
     * 保存通知记录
     */
    void saveNotificationRecord(NotificationRecord record);

    /**
     * 获取用户通知偏好设置
     */
    List<UserNotificationPreference> getUserNotificationPreferences(String userId);

    /**
     * 获取通知模板
     */
    NotificationTemplate getNotificationTemplate(String notificationType, String orderStatus);

    /**
     * 替换模板变量
     */
    String replaceTemplateVariables(String templateContent, Map<String, Object> variables);

    /**
     * 记录通知发送结果
     */
    void recordNotificationResult(NotificationRecord record);

    /**
     * 重试发送通知
     */
    void retrySendNotification(String notificationId);
}