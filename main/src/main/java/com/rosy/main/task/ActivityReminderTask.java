package com.rosy.main.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rosy.main.domain.entity.Activity;
import com.rosy.main.domain.entity.Enrollment;
import com.rosy.main.enums.ActivityStatusEnum;
import com.rosy.main.enums.EnrollmentStatusEnum;
import com.rosy.main.enums.NotificationTypeEnum;
import com.rosy.main.service.IActivityService;
import com.rosy.main.service.IEnrollmentService;
import com.rosy.main.service.INotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 活动提醒定时任务
 */
@Component
public class ActivityReminderTask {

    @Autowired
    private IActivityService activityService;
    
    @Autowired
    private IEnrollmentService enrollmentService;
    
    @Autowired
    private INotificationService notificationService;

    /**
     * 每小时检查一次即将开始的活动（1小时内）
     */
    @Scheduled(cron = "0 0 * * * ?")
    public void remindUpcomingActivities() {
        // 计算一小时后的时间
        LocalDateTime oneHourLater = LocalDateTime.now().plusHours(1);
        
        // 查询一小时后开始且状态为招募中的活动
        QueryWrapper<Activity> activityQuery = new QueryWrapper<>();
        activityQuery.eq("status", ActivityStatusEnum.RECRUITING.getCode());
        activityQuery.le("start_time", oneHourLater);
        activityQuery.gt("start_time", LocalDateTime.now());
        
        List<Activity> upcomingActivities = activityService.list(activityQuery);
        
        // 为每个即将开始的活动发送提醒通知
        for (Activity activity : upcomingActivities) {
            // 查询已报名并通过审核的用户
            QueryWrapper<Enrollment> enrollmentQuery = new QueryWrapper<>();
            enrollmentQuery.eq("activity_id", activity.getId());
            enrollmentQuery.eq("status", EnrollmentStatusEnum.APPROVED.getCode());
            
            List<Enrollment> enrollments = enrollmentService.list(enrollmentQuery);
            
            if (!enrollments.isEmpty()) {
                // 提取用户ID列表
                List<Long> userIds = enrollments.stream()
                        .map(Enrollment::getUserId)
                        .collect(Collectors.toList());
                
                // 发送活动提醒通知
                String title = "活动即将开始提醒";
                String content = String.format("您报名的活动《%s》将于1小时后开始，请准时参加。活动地点：%s", 
                        activity.getTitle(), activity.getLocation());
                
                notificationService.sendNotificationBatch(userIds, title, content, 
                        NotificationTypeEnum.ACTIVITY_REMINDER.getCode(), activity.getId());
            }
        }
    }

    /**
     * 每天凌晨2点检查并更新活动状态
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void updateActivityStatus() {
        LocalDateTime now = LocalDateTime.now();
        
        // 查询所有活动
        QueryWrapper<Activity> activityQuery = new QueryWrapper<>();
        activityQuery.ne("status", ActivityStatusEnum.COMPLETED.getCode());
        activityQuery.ne("status", ActivityStatusEnum.CANCELLED.getCode());
        
        List<Activity> activities = activityService.list(activityQuery);
        
        for (Activity activity : activities) {
            // 如果活动已结束，更新状态为已完成
            if (activity.getEndTime().isBefore(now)) {
                activityService.updateActivityStatus(activity.getId(), ActivityStatusEnum.COMPLETED.getCode());
            }
            // 如果活动已开始但未结束，更新状态为进行中
            else if (activity.getStartTime().isBefore(now) && activity.getEndTime().isAfter(now)) {
                activityService.updateActivityStatus(activity.getId(), ActivityStatusEnum.IN_PROGRESS.getCode());
            }
        }
    }
}