package com.rosy.main.service;

public interface INotificationService {

    void sendApprovalNotification(Long bookingId, boolean approved);

    void sendMeetingStartNotification(Long bookingId);
}
