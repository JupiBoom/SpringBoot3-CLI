package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.main.domain.entity.MeetingNotification;
import com.rosy.main.domain.entity.MeetingReservation;
import com.rosy.main.domain.enums.NotificationTypeEnum;
import com.rosy.main.domain.enums.ReservationStatusEnum;
import com.rosy.main.domain.vo.MeetingNotificationVO;
import com.rosy.main.mapper.MeetingNotificationMapper;
import com.rosy.main.service.IMeetingNotificationService;
import com.rosy.main.service.IMeetingReservationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MeetingNotificationServiceImpl extends ServiceImpl<MeetingNotificationMapper, MeetingNotification> implements IMeetingNotificationService {

    private final IMeetingReservationService meetingReservationService;

    public MeetingNotificationServiceImpl(IMeetingReservationService meetingReservationService) {
        this.meetingReservationService = meetingReservationService;
    }

    @Override
    public Boolean sendApprovalNotification(Long reservationId, Long userId, Boolean approved, String comment) {
        MeetingReservation reservation = meetingReservationService.getById(reservationId);
        if (reservation == null) {
            return false;
        }

        MeetingNotification notification = new MeetingNotification();
        notification.setReservationId(reservationId);
        notification.setUserId(userId);

        if (approved) {
            notification.setNotificationType(NotificationTypeEnum.APPROVAL_APPROVED.getCode().byteValue());
            notification.setTitle("会议预约已通过");
            notification.setContent("您的会议预约《" + reservation.getTitle() + "》已通过审批");
        } else {
            notification.setNotificationType(NotificationTypeEnum.APPROVAL_REJECTED.getCode().byteValue());
            notification.setTitle("会议预约已驳回");
            String content = "您的会议预约《" + reservation.getTitle() + "》已被驳回";
            if (comment != null && !comment.isEmpty()) {
                content += "，驳回原因：" + comment;
            }
            notification.setContent(content);
        }

        notification.setIsRead((byte) 0);
        return this.save(notification);
    }

    @Override
    public Boolean sendMeetingReminder(Long reservationId) {
        MeetingReservation reservation = meetingReservationService.getById(reservationId);
        if (reservation == null) {
            return false;
        }

        MeetingNotification notification = new MeetingNotification();
        notification.setReservationId(reservationId);
        notification.setUserId(reservation.getApplicantId());
        notification.setNotificationType(NotificationTypeEnum.MEETING_REMINDER.getCode().byteValue());
        notification.setTitle("会议即将开始");
        notification.setContent("您的会议《" + reservation.getTitle() + "》即将于" + reservation.getStartTime() + "开始，请准时参加");
        notification.setIsRead((byte) 0);
        return this.save(notification);
    }

    @Override
    public Boolean sendCancellationNotification(Long reservationId) {
        MeetingReservation reservation = meetingReservationService.getById(reservationId);
        if (reservation == null) {
            return false;
        }

        MeetingNotification notification = new MeetingNotification();
        notification.setReservationId(reservationId);
        notification.setUserId(reservation.getApplicantId());
        notification.setNotificationType(NotificationTypeEnum.MEETING_CANCELLED.getCode().byteValue());
        notification.setTitle("会议已取消");
        notification.setContent("您的会议《" + reservation.getTitle() + "》已取消");
        notification.setIsRead((byte) 0);
        return this.save(notification);
    }

    @Override
    public List<MeetingNotificationVO> getNotificationsByUser(Long userId) {
        QueryWrapper<MeetingNotification> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.orderByDesc("create_time");
        List<MeetingNotification> notifications = this.list(wrapper);
        return notifications.stream()
                .map(notification -> {
                    MeetingNotificationVO vo = BeanUtil.copyProperties(notification, MeetingNotificationVO.class);
                    vo.setNotificationTypeDesc(NotificationTypeEnum.getDescByCode(notification.getNotificationType().intValue()));
                    return vo;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<MeetingNotificationVO> getUnreadNotifications(Long userId) {
        QueryWrapper<MeetingNotification> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.eq("is_read", 0);
        wrapper.orderByDesc("create_time");
        List<MeetingNotification> notifications = this.list(wrapper);
        return notifications.stream()
                .map(notification -> {
                    MeetingNotificationVO vo = BeanUtil.copyProperties(notification, MeetingNotificationVO.class);
                    vo.setNotificationTypeDesc(NotificationTypeEnum.getDescByCode(notification.getNotificationType().intValue()));
                    return vo;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Boolean markAsRead(Long id) {
        MeetingNotification notification = this.getById(id);
        if (notification == null) {
            return false;
        }
        notification.setIsRead((byte) 1);
        return this.updateById(notification);
    }

    @Override
    public Boolean markAllAsRead(Long userId) {
        QueryWrapper<MeetingNotification> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.eq("is_read", 0);
        List<MeetingNotification> notifications = this.list(wrapper);
        for (MeetingNotification notification : notifications) {
            notification.setIsRead((byte) 1);
        }
        return this.updateBatchById(notifications);
    }
}
