package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.MeetingNotification;
import com.rosy.main.domain.vo.MeetingNotificationVO;

import java.util.List;

public interface IMeetingNotificationService extends IService<MeetingNotification> {

    Boolean sendApprovalNotification(Long reservationId, Long userId, Boolean approved, String comment);

    Boolean sendMeetingReminder(Long reservationId);

    Boolean sendCancellationNotification(Long reservationId);

    List<MeetingNotificationVO> getNotificationsByUser(Long userId);

    List<MeetingNotificationVO> getUnreadNotifications(Long userId);

    Boolean markAsRead(Long id);

    Boolean markAllAsRead(Long userId);
}
