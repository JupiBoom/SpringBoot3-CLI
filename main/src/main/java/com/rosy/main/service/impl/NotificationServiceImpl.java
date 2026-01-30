package com.rosy.main.service.impl;

import com.rosy.main.domain.entity.Booking;
import com.rosy.main.domain.entity.MeetingRoom;
import com.rosy.main.mapper.BookingMapper;
import com.rosy.main.mapper.MeetingRoomMapper;
import com.rosy.main.service.INotificationService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Slf4j
@Service
public class NotificationServiceImpl implements INotificationService {

    @Resource
    private BookingMapper bookingMapper;

    @Resource
    private MeetingRoomMapper meetingRoomMapper;

    @Override
    @Async
    public void sendApprovalNotification(Long bookingId, boolean approved) {
        Booking booking = bookingMapper.selectById(bookingId);
        if (booking == null) {
            log.error("Booking not found for notification: {}", bookingId);
            return;
        }

        MeetingRoom room = meetingRoomMapper.selectById(booking.getRoomId());
        String roomName = room != null ? room.getName() : "未知会议室";

        String status = approved ? "通过" : "驳回";
        String message = String.format("您的会议室预约申请已%s！\n会议室：%s\n时间：%s 至 %s\n主题：%s",
                status,
                roomName,
                booking.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                booking.getEndTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                booking.getSubject());

        log.info("发送审批通知 - 用户ID: {}, 消息: {}", booking.getUserId(), message);
    }

    @Override
    @Async
    public void sendMeetingStartNotification(Long bookingId) {
        Booking booking = bookingMapper.selectById(bookingId);
        if (booking == null) {
            log.error("Booking not found for meeting start notification: {}", bookingId);
            return;
        }

        MeetingRoom room = meetingRoomMapper.selectById(booking.getRoomId());
        String roomName = room != null ? room.getName() : "未知会议室";

        String message = String.format("会议即将开始！\n会议室：%s\n时间：%s 至 %s\n主题：%s\n请准时参加！",
                roomName,
                booking.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                booking.getEndTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                booking.getSubject());

        log.info("发送会议开始通知 - 用户ID: {}, 消息: {}", booking.getUserId(), message);
    }
}
