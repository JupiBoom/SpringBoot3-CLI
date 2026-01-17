package com.rosy.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.main.domain.entity.Notification;
import com.rosy.main.mapper.NotificationMapper;
import com.rosy.main.service.INotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 通知Service实现类
 */
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl extends ServiceImpl<NotificationMapper, Notification> implements INotificationService {

    private final NotificationMapper notificationMapper;

    @Override
    public Notification createNotification(Long userId, String userName, String title, String content, String type, Long relatedId, String relatedType) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setUserName(userName);
        notification.setTitle(title);
        notification.setContent(content);
        notification.setType(type);
        notification.setRelatedId(relatedId);
        notification.setRelatedType(relatedType);
        notification.setStatus("unread");
        notificationMapper.insert(notification);
        return notification;
    }

    @Override
    public List<Notification> getUserNotifications(Long userId, Integer limit) {
        Page<Notification> page = new Page<>(1, limit != null ? limit : 50);
        LambdaQueryWrapper<Notification> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Notification::getUserId, userId);
        wrapper.orderByDesc(Notification::getCreateTime);
        return notificationMapper.selectPage(page, wrapper).getRecords();
    }

    @Override
    public boolean markAsRead(Long id, Long userId) {
        Notification notification = notificationMapper.selectById(id);
        if (notification != null && userId.equals(notification.getUserId())) {
            notification.setStatus("read");
            notificationMapper.updateById(notification);
            return true;
        }
        return false;
    }

    @Override
    public boolean markAllAsRead(Long userId) {
        LambdaQueryWrapper<Notification> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Notification::getUserId, userId);
        wrapper.eq(Notification::getStatus, "unread");
        List<Notification> notifications = notificationMapper.selectList(wrapper);
        for (Notification notification : notifications) {
            notification.setStatus("read");
            notificationMapper.updateById(notification);
        }
        return true;
    }

    @Override
    public Long getUnreadCount(Long userId) {
        LambdaQueryWrapper<Notification> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Notification::getUserId, userId);
        wrapper.eq(Notification::getStatus, "unread");
        return notificationMapper.selectCount(wrapper);
    }
}
