package com.rosy.web.job;

import com.rosy.main.domain.entity.Registration;
import com.rosy.main.service.IRegistrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 活动提醒定时任务
 */
@Component
@Slf4j
public class ActivityReminderJob {

    @Autowired
    private IRegistrationService registrationService;

    /**
     * 每5分钟执行一次，检查需要发送提醒的报名记录
     */
    @Scheduled(fixedRate = 5 * 60 * 1000)
    public void sendActivityReminders() {
        log.info("开始执行活动提醒定时任务");

        try {
            List<Registration> needReminderList = registrationService.getNeedReminderList();

            if (needReminderList.isEmpty()) {
                log.info("没有需要发送提醒的报名记录");
                return;
            }

            log.info("发现 {} 条需要发送提醒的报名记录", needReminderList.size());

            for (Registration registration : needReminderList) {
                try {
                    registrationService.sendActivityReminder(registration.getId());
                    log.info("成功发送活动提醒，报名ID：{}，用户ID：{}，活动ID：{}",
                            registration.getId(), registration.getUserId(), registration.getActivityId());
                } catch (Exception e) {
                    log.error("发送活动提醒失败，报名ID：{}，错误：{}", registration.getId(), e.getMessage());
                }
            }

            log.info("活动提醒定时任务执行完成");
        } catch (Exception e) {
            log.error("活动提醒定时任务执行失败", e);
        }
    }
}
