package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.Notification;

import java.util.List;

/**
 * 通知Service接口
 */
public interface INotificationService extends IService<Notification> {

    Notification createNotification(Long userId, String userName, String title, String content, String type, Long relatedId, String relatedType);

    List<Notification> getUserNotifications(Long userId, Integer limit);

    boolean markAsRead(Long id, Long userId);

    boolean markAllAsRead(Long userId);

    Long getUnreadCount(Long userId);
}
