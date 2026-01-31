package com.rosy.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.main.domain.entity.Notification;
import com.rosy.main.enums.NotificationTypeEnum;
import com.rosy.main.mapper.NotificationMapper;
import com.rosy.main.service.INotificationService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 通知Service实现类
 */
@Service
public class NotificationServiceImpl extends ServiceImpl<NotificationMapper, Notification> implements INotificationService {

    @Override
    public Page<Notification> getNotificationPage(Page<Notification> page, Long userId, Integer type, Integer isRead) {
        QueryWrapper<Notification> queryWrapper = new QueryWrapper<>();
        
        // 添加查询条件
        if (userId != null) {
            queryWrapper.eq("user_id", userId);
        }
        if (type != null) {
            queryWrapper.eq("type", type);
        }
        if (isRead != null) {
            queryWrapper.eq("is_read", isRead);
        }
        
        // 按创建时间倒序排列
        queryWrapper.orderByDesc("create_time");
        
        return this.page(page, queryWrapper);
    }

    @Override
    public boolean sendNotification(Long userId, String title, String content, Integer type, Long relatedId) {
        // 验证通知类型是否合法
        NotificationTypeEnum typeEnum = NotificationTypeEnum.getByCode(type);
        if (typeEnum == null) {
            return false;
        }
        
        // 创建通知
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setTitle(title);
        notification.setContent(content);
        notification.setType(type);
        notification.setIsRead(0); // 默认未读
        notification.setRelatedId(relatedId);
        
        return this.save(notification);
    }

    @Override
    public boolean sendNotificationBatch(List<Long> userIds, String title, String content, Integer type, Long relatedId) {
        // 验证通知类型是否合法
        NotificationTypeEnum typeEnum = NotificationTypeEnum.getByCode(type);
        if (typeEnum == null) {
            return false;
        }
        
        // 批量创建通知
        for (Long userId : userIds) {
            Notification notification = new Notification();
            notification.setUserId(userId);
            notification.setTitle(title);
            notification.setContent(content);
            notification.setType(type);
            notification.setIsRead(0); // 默认未读
            notification.setRelatedId(relatedId);
            
            this.save(notification);
        }
        
        return true;
    }

    @Override
    public boolean markAsRead(Long notificationId) {
        UpdateWrapper<Notification> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", notificationId);
        updateWrapper.set("is_read", 1);
        
        return this.update(updateWrapper);
    }

    @Override
    public boolean markAsReadBatch(List<Long> notificationIds) {
        if (notificationIds == null || notificationIds.isEmpty()) {
            return true;
        }
        
        UpdateWrapper<Notification> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("id", notificationIds);
        updateWrapper.set("is_read", 1);
        
        return this.update(updateWrapper);
    }

    @Override
    public boolean markAllAsRead(Long userId) {
        UpdateWrapper<Notification> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("user_id", userId);
        updateWrapper.set("is_read", 1);
        
        return this.update(updateWrapper);
    }

    @Override
    public long getUnreadCount(Long userId) {
        QueryWrapper<Notification> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("is_read", 0);
        
        return this.count(queryWrapper);
    }
}