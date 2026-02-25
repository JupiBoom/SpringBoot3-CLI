package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.Notification;

import java.util.List;

public interface INotificationService extends IService<Notification> {
    Notification create(Long userId, String type, String title, String content, Long relatedId);
    List<Notification> getMyNotifications(Long userId);
    void markAsRead(Long notificationId, Long userId);
    int countUnread(Long userId);
}
