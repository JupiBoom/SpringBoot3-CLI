package com.rosy.meeting.service;

import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.meeting.domain.entity.Notification;

public interface INotificationService {
    void sendApprovalNotification(Long userId, Long reservationId, String meetingSubject);
    
    void sendApprovalResultNotification(Long userId, Long reservationId, String meetingSubject, boolean approved, String reason);
    
    void sendMeetingReminder(Long userId, Long reservationId, String meetingSubject);
    
    ApiResponse getNotifications(Long userId);
    
    ApiResponse markAsRead(Long notificationId);
    
    ApiResponse deleteNotification(Long notificationId);
}