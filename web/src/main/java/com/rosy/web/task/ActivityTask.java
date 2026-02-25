package com.rosy.web.task;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rosy.common.enums.ActivityStatusEnum;
import com.rosy.common.enums.NotificationTypeEnum;
import com.rosy.main.domain.entity.Activity;
import com.rosy.main.domain.entity.Notification;
import com.rosy.main.domain.entity.Registration;
import com.rosy.main.mapper.ActivityMapper;
import com.rosy.main.mapper.NotificationMapper;
import com.rosy.main.mapper.RegistrationMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Component
public class ActivityTask {

    @Resource
    private ActivityMapper activityMapper;

    @Resource
    private RegistrationMapper registrationMapper;

    @Resource
    private NotificationMapper notificationMapper;

    @Scheduled(fixedRate = 60000)
    @Transactional(rollbackFor = Exception.class)
    public void updateActivityStatus() {
        LocalDateTime now = LocalDateTime.now();
        
        LambdaQueryWrapper<Activity> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(Activity::getStatus, ActivityStatusEnum.RECRUITING.name(), ActivityStatusEnum.ONGOING.name());
        List<Activity> activities = activityMapper.selectList(wrapper);
        
        for (Activity activity : activities) {
            String newStatus = null;
            
            if (now.isAfter(activity.getEndTime())) {
                newStatus = ActivityStatusEnum.COMPLETED.name();
            } else if (now.isAfter(activity.getStartTime()) && 
                       ActivityStatusEnum.RECRUITING.name().equals(activity.getStatus())) {
                newStatus = ActivityStatusEnum.ONGOING.name();
            }
            
            if (newStatus != null && !newStatus.equals(activity.getStatus())) {
                activity.setStatus(newStatus);
                activityMapper.updateById(activity);
                log.info("活动状态更新: activityId={}, newStatus={}", activity.getId(), newStatus);
            }
        }
    }

    @Scheduled(fixedRate = 60000)
    @Transactional(rollbackFor = Exception.class)
    public void sendReminderNotifications() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneHourLater = now.plus(1, ChronoUnit.HOURS);
        
        LambdaQueryWrapper<Activity> activityWrapper = new LambdaQueryWrapper<>();
        activityWrapper.between(Activity::getStartTime, now, oneHourLater);
        activityWrapper.eq(Activity::getStatus, ActivityStatusEnum.RECRUITING.name());
        List<Activity> activities = activityMapper.selectList(activityWrapper);
        
        for (Activity activity : activities) {
            LambdaQueryWrapper<Registration> regWrapper = new LambdaQueryWrapper<>();
            regWrapper.eq(Registration::getActivityId, activity.getId());
            regWrapper.eq(Registration::getReminded, 0);
            List<Registration> registrations = registrationMapper.selectList(regWrapper);
            
            for (Registration registration : registrations) {
                boolean notificationExists = checkNotificationExists(registration.getUserId(), activity.getId());
                if (!notificationExists) {
                    Notification notification = new Notification();
                    notification.setUserId(registration.getUserId());
                    notification.setType(NotificationTypeEnum.ACTIVITY.name());
                    notification.setTitle("活动提醒");
                    notification.setContent("您报名的活动【" + activity.getTitle() + "】将在1小时后开始，请准时参加！地点：" + activity.getLocation());
                    notification.setRelatedId(activity.getId());
                    notification.setIsRead(0);
                    notificationMapper.insert(notification);
                    
                    registration.setReminded(1);
                    registrationMapper.updateById(registration);
                    
                    log.info("发送提醒通知: activityId={}, userId={}", activity.getId(), registration.getUserId());
                }
            }
        }
    }
    
    private boolean checkNotificationExists(Long userId, Long activityId) {
        LambdaQueryWrapper<Notification> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Notification::getUserId, userId);
        wrapper.eq(Notification::getRelatedId, activityId);
        wrapper.eq(Notification::getTitle, "活动提醒");
        return notificationMapper.selectCount(wrapper) > 0;
    }
}
