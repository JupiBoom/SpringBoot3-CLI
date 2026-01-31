package com.rosy.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.main.domain.entity.RepairNotification;
import com.rosy.main.mapper.RepairNotificationMapper;
import com.rosy.main.service.IRepairNotificationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RepairNotificationServiceImpl extends ServiceImpl<RepairNotificationMapper, RepairNotification> implements IRepairNotificationService {

    @Override
    public void sendNotification(Long orderId, Long userId, Byte notifyType, String title, String content) {
        RepairNotification notification = new RepairNotification();
        notification.setOrderId(orderId);
        notification.setUserId(userId);
        notification.setNotifyType(notifyType);
        notification.setTitle(title);
        notification.setContent(content);
        notification.setIsRead((byte) 0);
        save(notification);
    }

    @Override
    public void markAsRead(Long notificationId) {
        RepairNotification notification = getById(notificationId);
        if (notification != null) {
            notification.setIsRead((byte) 1);
            notification.setReadTime(LocalDateTime.now());
            updateById(notification);
        }
    }

    @Override
    public int getUnreadCount(Long userId) {
        LambdaQueryWrapper<RepairNotification> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RepairNotification::getUserId, userId)
               .eq(RepairNotification::getIsRead, 0);
        return (int) count(wrapper);
    }
}
