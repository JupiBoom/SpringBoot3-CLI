package com.rosy.main.task;

import com.rosy.common.enums.ActivityStatusEnum;
import com.rosy.common.enums.NotificationTypeEnum;
import com.rosy.main.domain.entity.Activity;
import com.rosy.main.domain.entity.Notification;
import com.rosy.main.domain.entity.Registration;
import com.rosy.main.mapper.ActivityMapper;
import com.rosy.main.mapper.NotificationMapper;
import com.rosy.main.mapper.RegistrationMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ActivityScheduledTask {

    private final ActivityMapper activityMapper;
    private final RegistrationMapper registrationMapper;
    private final NotificationMapper notificationMapper;

    @Scheduled(fixedRate = 60000)
    @Transactional(rollbackFor = Exception.class)
    public void updateActivityStatus() {
        log.info("开始执行活动状态更新任务");
        
        LocalDateTime now = LocalDateTime.now();
        
        List<Activity> activities = activityMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<Activity>()
                        .in(Activity::getStatus, 
                            ActivityStatusEnum.RECRUITING.getValue(), 
                            ActivityStatusEnum.ONGOING.getValue()));

        for (Activity activity : activities) {
            int newStatus = activity.getStatus();
            
            if (ActivityStatusEnum.RECRUITING.getValue().equals(activity.getStatus())) {
                if (now.isAfter(activity.getStartTime()) && now.isBefore(activity.getEndTime())) {
                    newStatus = ActivityStatusEnum.ONGOING.getValue();
                } else if (now.isAfter(activity.getEndTime())) {
                    newStatus = ActivityStatusEnum.COMPLETED.getValue();
                }
            } else if (ActivityStatusEnum.ONGOING.getValue().equals(activity.getStatus())) {
                if (now.isAfter(activity.getEndTime())) {
                    newStatus = ActivityStatusEnum.COMPLETED.getValue();
                }
            }
            
            if (newStatus != activity.getStatus()) {
                activity.setStatus(newStatus);
                activityMapper.updateById(activity);
                log.info("活动[{}]状态更新为: {}", activity.getTitle(), 
                        ActivityStatusEnum.fromValue(newStatus).getDesc());
            }
        }
        
        log.info("活动状态更新任务执行完成");
    }

    @Scheduled(fixedRate = 300000)
    @Transactional(rollbackFor = Exception.class)
    public void sendActivityReminder() {
        log.info("开始执行活动提醒任务");
        
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneHourLater = now.plusHours(1);
        
        List<Activity> activities = activityMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<Activity>()
                        .eq(Activity::getStatus, ActivityStatusEnum.RECRUITING.getValue())
                        .ge(Activity::getStartTime, now)
                        .le(Activity::getStartTime, oneHourLater));

        for (Activity activity : activities) {
            List<Registration> registrations = registrationMapper.selectList(
                    new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<Registration>()
                            .eq(Registration::getActivityId, activity.getId())
                            .eq(Registration::getStatus, com.rosy.common.enums.RegistrationStatusEnum.APPROVED.getValue()));

            for (Registration registration : registrations) {
                Notification existingNotification = notificationMapper.selectOne(
                        new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<Notification>()
                                .eq(Notification::getUserId, registration.getUserId())
                                .eq(Notification::getActivityId, activity.getId())
                                .eq(Notification::getType, NotificationTypeEnum.ACTIVITY_REMINDER.getValue()));

                if (existingNotification == null) {
                    Notification notification = new Notification();
                    notification.setUserId(registration.getUserId());
                    notification.setActivityId(activity.getId());
                    notification.setType(NotificationTypeEnum.ACTIVITY_REMINDER.getValue());
                    notification.setTitle("活动开始提醒");
                    notification.setContent(String.format("您报名的活动「%s」将在1小时后开始，请准时参加！活动地点：%s", 
                            activity.getTitle(), activity.getLocation()));
                    notification.setIsRead(0);
                    notification.setCreatedTime(LocalDateTime.now());
                    notificationMapper.insert(notification);
                    
                    log.info("为用户[{}]发送活动[{}]提醒", registration.getUserId(), activity.getTitle());
                }
            }
        }
        
        log.info("活动提醒任务执行完成");
    }
}
