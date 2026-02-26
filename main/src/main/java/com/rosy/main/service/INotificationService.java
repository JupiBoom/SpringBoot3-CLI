package com.rosy.main.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.Notification;
import com.rosy.main.domain.vo.NotificationVO;

import java.util.List;

public interface INotificationService extends IService<Notification> {

    NotificationVO getNotificationVO(Notification notification);

    void sendActivityReminder(Long activityId, Long userId);

    void sendRegistrationAuditNotice(Long registrationId, boolean approved);

    void sendSystemNotice(Long userId, String title, String content);

    List<NotificationVO> getUnreadNotifications(Long userId);

    boolean markAsRead(Long notificationId);

    boolean markAllAsRead(Long userId);
}
