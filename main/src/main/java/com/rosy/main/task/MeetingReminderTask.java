package com.rosy.main.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rosy.main.domain.entity.MeetingReservation;
import com.rosy.main.domain.enums.ReservationStatusEnum;
import com.rosy.main.service.IMeetingNotificationService;
import com.rosy.main.service.IMeetingReservationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class MeetingReminderTask {

    private final IMeetingReservationService meetingReservationService;
    private final IMeetingNotificationService meetingNotificationService;

    public MeetingReminderTask(IMeetingReservationService meetingReservationService,
                                IMeetingNotificationService meetingNotificationService) {
        this.meetingReservationService = meetingReservationService;
        this.meetingNotificationService = meetingNotificationService;
    }

    @Scheduled(cron = "0 */5 * * * ?")
    public void sendMeetingReminders() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime reminderTime = now.plusMinutes(15);

        QueryWrapper<MeetingReservation> wrapper = new QueryWrapper<>();
        wrapper.eq("status", ReservationStatusEnum.APPROVED.getCode());
        wrapper.ge("start_time", now);
        wrapper.le("start_time", reminderTime);

        List<MeetingReservation> reservations = meetingReservationService.list(wrapper);

        for (MeetingReservation reservation : reservations) {
            QueryWrapper<com.rosy.main.domain.entity.MeetingNotification> notificationWrapper = new QueryWrapper<>();
            notificationWrapper.eq("reservation_id", reservation.getId());
            notificationWrapper.eq("notification_type", 3);

            if (meetingNotificationService.count(notificationWrapper) == 0) {
                meetingNotificationService.sendMeetingReminder(reservation.getId());
            }
        }
    }
}
