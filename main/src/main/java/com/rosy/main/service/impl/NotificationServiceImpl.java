package com.rosy.main.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.main.domain.entity.Notification;
import com.rosy.main.mapper.NotificationMapper;
import com.rosy.main.service.INotificationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl extends ServiceImpl<NotificationMapper, Notification> implements INotificationService {

    @Override
    public Notification create(Long userId, String type, String title, String content, Long relatedId) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setType(type);
        notification.setTitle(title);
        notification.setContent(content);
        notification.setRelatedId(relatedId);
        notification.setIsRead(0);
        save(notification);
        return notification;
    }

    @Override
    public List<Notification> getMyNotifications(Long userId) {
        return lambdaQuery()
                .eq(Notification::getUserId, userId)
                .orderByDesc(Notification::getCreateTime)
                .list();
    }

    @Override
    public void markAsRead(Long notificationId, Long userId) {
        Notification notification = getById(notificationId);
        if (notification != null && notification.getUserId().equals(userId)) {
            notification.setIsRead(1);
            updateById(notification);
        }
    }

    @Override
    public int countUnread(Long userId) {
        return Math.toIntExact(lambdaQuery()
                .eq(Notification::getUserId, userId)
                .eq(Notification::getIsRead, 0)
                .count());
    }
}
