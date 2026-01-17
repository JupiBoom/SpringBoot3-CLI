package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.Notification;

import java.util.List;

public interface INotificationService extends IService<Notification> {

    void sendNotification(Long receiverId, String receiverName, String title, String content, Byte type, Long relatedId);

    void markAsRead(Long notificationId);

    void markAllAsRead(Long userId);

    List<Notification> getUnreadNotifications(Long userId);

    List<Notification> getAllNotifications(Long userId);
}
