package com.rosy.main.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.entity.Notification;
import com.rosy.main.mapper.NotificationMapper;
import com.rosy.main.service.INotificationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationServiceImpl extends ServiceImpl<NotificationMapper, Notification> implements INotificationService {

    @Override
    public void sendNotification(Long receiverId, String receiverName, String title, String content, Byte type, Long relatedId) {
        if (receiverId == null) {
            return;
        }

        Notification notification = new Notification();
        notification.setReceiverId(receiverId);
        notification.setReceiverName(receiverName);
        notification.setTitle(title);
        notification.setContent(content);
        notification.setType(type);
        notification.setRelatedId(relatedId);
        notification.setIsRead((byte) 0);

        boolean saved = save(notification);
        if (!saved) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "发送通知失败");
        }
    }

    @Override
    public void markAsRead(Long notificationId) {
        Notification notification = getById(notificationId);
        if (notification == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "通知不存在");
        }

        notification.setIsRead((byte) 1);
        notification.setReadTime(LocalDateTime.now());
        boolean updated = updateById(notification);
        if (!updated) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "标记已读失败");
        }
    }

    @Override
    public void markAllAsRead(Long userId) {
        lambdaUpdate()
                .eq(Notification::getReceiverId, userId)
                .eq(Notification::getIsRead, 0)
                .set(Notification::getIsRead, 1)
                .set(Notification::getReadTime, LocalDateTime.now())
                .update();
    }

    @Override
    public List<Notification> getUnreadNotifications(Long userId) {
        return lambdaQuery()
                .eq(Notification::getReceiverId, userId)
                .eq(Notification::getIsRead, 0)
                .orderByDesc(Notification::getCreateTime)
                .list();
    }

    @Override
    public List<Notification> getAllNotifications(Long userId) {
        return lambdaQuery()
                .eq(Notification::getReceiverId, userId)
                .orderByDesc(Notification::getCreateTime)
                .list();
    }
}
