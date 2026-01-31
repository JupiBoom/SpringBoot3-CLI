package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.RepairNotification;

public interface IRepairNotificationService extends IService<RepairNotification> {

    void sendNotification(Long orderId, Long userId, Byte notifyType, String title, String content);

    void markAsRead(Long notificationId);

    int getUnreadCount(Long userId);
}
