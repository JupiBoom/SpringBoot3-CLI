package com.rosy.framework.task;

import com.rosy.main.domain.entity.MeetingBooking;
import com.rosy.main.domain.entity.Notification;
import com.rosy.main.domain.entity.User;
import com.rosy.main.mapper.MeetingBookingMapper;
import com.rosy.main.mapper.MeetingRoomMapper;
import com.rosy.main.mapper.NotificationMapper;
import com.rosy.main.mapper.UserMapper;
import com.rosy.main.service.IRoomUsageStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 会议室管理系统定时任务
 */
@Component
public class MeetingRoomTask {

    @Autowired
    private IRoomUsageStatsService roomUsageStatsService;
    
    @Autowired
    private MeetingBookingMapper meetingBookingMapper;
    
    @Autowired
    private MeetingRoomMapper meetingRoomMapper;
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private NotificationMapper notificationMapper;

    /**
     * 每天凌晨1点生成前一天的统计数据
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void generateDailyStats() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        roomUsageStatsService.generateDailyStats(yesterday);
    }

    /**
     * 每15分钟检查即将开始的会议，发送提醒通知
     */
    @Scheduled(cron = "0 */15 * * * ?")
    public void sendMeetingReminder() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime reminderTime = now.plusMinutes(15);
        
        // 查询15分钟后开始的会议
        List<MeetingBooking> upcomingMeetings = meetingBookingMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<MeetingBooking>()
                        .eq("status", 1) // 已通过的会议
                        .ge("start_time", now) // 开始时间大于等于当前时间
                        .le("start_time", reminderTime) // 开始时间小于等于15分钟后
                        .and(wrapper -> wrapper
                                .isNull("reminder_sent") // 未发送提醒
                                .or()
                                .eq("reminder_sent", 0)) // 或者提醒标记为0
        );
        
        for (MeetingBooking booking : upcomingMeetings) {
            // 发送提醒给预约人
            sendReminderToBooker(booking);
            
            // 发送提醒给参会人员
            sendReminderToAttendees(booking);
            
            // 标记已发送提醒
            booking.setReminderSent(1);
            meetingBookingMapper.updateById(booking);
        }
    }

    /**
     * 发送提醒给预约人
     */
    private void sendReminderToBooker(MeetingBooking booking) {
        // 获取会议室名称
        String roomName = "未知会议室";
        if (booking.getRoomId() != null) {
            var room = meetingRoomMapper.selectById(booking.getRoomId());
            if (room != null) {
                roomName = room.getName();
            }
        }
        
        // 创建通知
        Notification notification = new Notification();
        notification.setUserId(booking.getBookerId());
        notification.setTitle("会议即将开始");
        notification.setContent(String.format("您预约的会议即将开始，会议室：%s，时间：%s 至 %s，请准时参加。", 
                roomName,
                booking.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                booking.getEndTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));
        notification.setType(1); // 会议开始前通知
        notification.setRelatedId(booking.getId());
        notification.setIsRead(0);
        
        notificationMapper.insert(notification);
    }

    /**
     * 发送提醒给参会人员
     */
    private void sendReminderToAttendees(MeetingBooking booking) {
        if (booking.getAttendees() == null || booking.getAttendees().isEmpty()) {
            return;
        }
        
        // 获取会议室名称
        String roomName = "未知会议室";
        if (booking.getRoomId() != null) {
            var room = meetingRoomMapper.selectById(booking.getRoomId());
            if (room != null) {
                roomName = room.getName();
            }
        }
        
        // 获取预约人姓名
        String bookerName = "未知用户";
        if (booking.getBookerId() != null) {
            User booker = userMapper.selectById(booking.getBookerId());
            if (booker != null) {
                bookerName = booker.getRealName();
            }
        }
        
        // 解析参会人员ID列表
        String[] attendeeIds = booking.getAttendees().split(",");
        
        for (String attendeeIdStr : attendeeIds) {
            try {
                Long attendeeId = Long.parseLong(attendeeIdStr.trim());
                
                // 获取参会人员信息
                User attendee = userMapper.selectById(attendeeId);
                if (attendee != null) {
                    // 创建通知
                    Notification notification = new Notification();
                    notification.setUserId(attendeeId);
                    notification.setTitle("会议即将开始");
                    notification.setContent(String.format("%s 邀请您参加的会议即将开始，会议室：%s，时间：%s 至 %s，请准时参加。", 
                            bookerName,
                            roomName,
                            booking.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                            booking.getEndTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));
                    notification.setType(1); // 会议开始前通知
                    notification.setRelatedId(booking.getId());
                    notification.setIsRead(0);
                    
                    notificationMapper.insert(notification);
                }
            } catch (NumberFormatException e) {
                // 忽略无效的ID
            }
        }
    }
}