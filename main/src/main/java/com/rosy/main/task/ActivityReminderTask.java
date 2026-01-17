package com.rosy.main.task;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rosy.main.domain.entity.Activity;
import com.rosy.main.domain.entity.Registration;
import com.rosy.main.domain.entity.User;
import com.rosy.main.service.IActivityService;
import com.rosy.main.service.IRegistrationService;
import com.rosy.main.service.IUserService;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ActivityReminderTask {

    private static final Logger logger = LoggerFactory.getLogger(ActivityReminderTask.class);

    @Resource
    private IActivityService activityService;

    @Resource
    private IRegistrationService registrationService;

    @Resource
    private IUserService userService;

    @Scheduled(cron = "0 0/10 * * * ?")
    public void remindActivityStart() {
        logger.info("开始执行活动开始提醒任务");

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneHourLater = now.plusHours(1);

        LambdaQueryWrapper<Activity> activityWrapper = new LambdaQueryWrapper<>();
        activityWrapper.eq(Activity::getStatus, (byte) 0)
                .ge(Activity::getStartTime, now)
                .le(Activity::getStartTime, oneHourLater);
        List<Activity> activities = activityService.list(activityWrapper);

        for (Activity activity : activities) {
            remindParticipants(activity);
        }

        logger.info("活动开始提醒任务执行完成，共处理{}个活动", activities.size());
    }

    private void remindParticipants(Activity activity) {
        LambdaQueryWrapper<Registration> registrationWrapper = new LambdaQueryWrapper<>();
        registrationWrapper.eq(Registration::getActivityId, activity.getId())
                .eq(Registration::getStatus, (byte) 1)
                .eq(Registration::getIsNotified, (byte) 0);
        List<Registration> registrations = registrationService.list(registrationWrapper);

        for (Registration registration : registrations) {
            User user = userService.getById(registration.getUserId());
            if (user != null) {
                sendReminder(user, activity);
                registration.setIsNotified((byte) 1);
                registrationService.updateById(registration);
            }
        }

        logger.info("活动【{}】已向{}名志愿者发送提醒", activity.getTitle(), registrations.size());
    }

    private void sendReminder(User user, Activity activity) {
        logger.info("发送提醒给志愿者【{}】，活动【{}】将于{}开始", 
                user.getRealName(), activity.getTitle(), activity.getStartTime());
    }
}
