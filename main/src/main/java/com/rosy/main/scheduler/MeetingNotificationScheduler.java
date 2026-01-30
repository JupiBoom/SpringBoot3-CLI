package com.rosy.main.scheduler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rosy.common.enums.BookingStatusEnum;
import com.rosy.main.domain.entity.Booking;
import com.rosy.main.service.IBookingService;
import com.rosy.main.service.INotificationService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
public class MeetingNotificationScheduler {

    @Resource
    private IBookingService bookingService;

    @Resource
    private INotificationService notificationService;

    @Scheduled(fixedRate = 60000)
    public void checkUpcomingMeetings() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime notificationTime = now.plusMinutes(30);

        LambdaQueryWrapper<Booking> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Booking::getStatus, BookingStatusEnum.APPROVED.getCode())
                .ge(Booking::getStartTime, now)
                .le(Booking::getStartTime, notificationTime);

        List<Booking> upcomingBookings = bookingService.list(queryWrapper);

        for (Booking booking : upcomingBookings) {
            try {
                notificationService.sendMeetingStartNotification(booking.getId());
            } catch (Exception e) {
                log.error("发送会议开始通知失败 - bookingId: {}", booking.getId(), e);
            }
        }
    }
}
