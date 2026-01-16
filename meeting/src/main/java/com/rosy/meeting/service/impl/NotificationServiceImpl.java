package com.rosy.meeting.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.meeting.domain.entity.Notification;
import com.rosy.meeting.mapper.NotificationMapper;
import com.rosy.meeting.service.INotificationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationServiceImpl implements INotificationService {
    private final NotificationMapper notificationMapper;

    public NotificationServiceImpl(NotificationMapper notificationMapper) {
        this.notificationMapper = notificationMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendApprovalNotification(Long userId, Long reservationId, String meetingSubject) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setNotificationType(1);
        notification.setTitle("预约申请提交成功");
        notification.setContent(String.format("您的会议预约《%s》已提交，等待审批。", meetingSubject));
        notification.setRelatedId(reservationId);
        notification.setIsRead(0);
        
        notificationMapper.insert(notification);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendApprovalResultNotification(Long userId, Long reservationId, String meetingSubject, boolean approved, String reason) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setNotificationType(1);
        notification.setTitle(approved ? "预约申请已通过" : "预约申请已驳回");
        notification.setContent(String.format("您的会议预约《%s》%s。%s", 
            meetingSubject, approved ? "已通过审批" : "已被驳回", 
            reason != null ? "原因：" + reason : ""));
        notification.setRelatedId(reservationId);
        notification.setIsRead(0);
        
        notificationMapper.insert(notification);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendMeetingReminder(Long userId, Long reservationId, String meetingSubject) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setNotificationType(2);
        notification.setTitle("会议即将开始");
        notification.setContent(String.format("温馨提醒：您预约的会议《%s》即将开始，请提前做好准备。", meetingSubject));
        notification.setRelatedId(reservationId);
        notification.setIsRead(0);
        
        notificationMapper.insert(notification);
    }

    @Override
    public ApiResponse getNotifications(Long userId) {
        LambdaQueryWrapper<Notification> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Notification::getUserId, userId);
        wrapper.orderByDesc(Notification::getCreatedTime);
        
        List<Notification> notifications = notificationMapper.selectList(wrapper);
        return ApiResponse.success(notifications);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse markAsRead(Long notificationId) {
        Notification notification = notificationMapper.selectById(notificationId);
        if (notification == null) {
            return ApiResponse.error("通知不存在");
        }
        
        notification.setIsRead(1);
        notification.setReadTime(LocalDateTime.now());
        notificationMapper.updateById(notification);
        
        return ApiResponse.success("已标记为已读");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse deleteNotification(Long notificationId) {
        int deleted = notificationMapper.deleteById(notificationId);
        if (deleted > 0) {
            return ApiResponse.success("删除成功");
        }
        return ApiResponse.error("删除失败");
    }
}