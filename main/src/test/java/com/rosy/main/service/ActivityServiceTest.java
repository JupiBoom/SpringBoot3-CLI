package com.rosy.main.service;

import com.rosy.main.domain.entity.Activity;
import com.rosy.main.enums.ActivityCategoryEnum;
import com.rosy.main.enums.ActivityStatusEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class ActivityServiceTest {

    @Autowired
    private IActivityService activityService;

    @Test
    public void testCreateActivity() {
        // 创建活动
        Activity activity = new Activity();
        activity.setTitle("环保志愿活动");
        activity.setDescription("清理公园垃圾，美化环境");
        activity.setStartTime(LocalDateTime.now().plusDays(1));
        activity.setEndTime(LocalDateTime.now().plusDays(1).plusHours(3));
        activity.setLocation("中心公园");
        activity.setRequiredPeople(20);
        activity.setCategory(ActivityCategoryEnum.ENVIRONMENTAL.getCode());
        activity.setOrganizerId(1L);

        // 保存活动
        boolean result = activityService.save(activity);
        assertTrue(result);
        assertNotNull(activity.getId());

        // 验证活动信息
        Activity savedActivity = activityService.getById(activity.getId());
        assertNotNull(savedActivity);
        assertEquals("环保志愿活动", savedActivity.getTitle());
        assertEquals(ActivityCategoryEnum.ENVIRONMENTAL.getCode(), savedActivity.getCategory());
        assertEquals(ActivityStatusEnum.RECRUITING.getCode(), savedActivity.getStatus());
    }

    @Test
    public void testUpdateActivityStatus() {
        // 创建活动
        Activity activity = new Activity();
        activity.setTitle("助老志愿活动");
        activity.setDescription("探访养老院，陪伴老人");
        activity.setStartTime(LocalDateTime.now().plusDays(2));
        activity.setEndTime(LocalDateTime.now().plusDays(2).plusHours(2));
        activity.setLocation("阳光养老院");
        activity.setRequiredPeople(10);
        activity.setCategory(ActivityCategoryEnum.ELDERLY.getCode());
        activity.setOrganizerId(1L);

        // 保存活动
        activityService.save(activity);

        // 更新活动状态为进行中
        boolean result = activityService.updateActivityStatus(activity.getId(), ActivityStatusEnum.IN_PROGRESS.getCode());
        assertTrue(result);

        // 验证状态更新
        Activity updatedActivity = activityService.getById(activity.getId());
        assertEquals(ActivityStatusEnum.IN_PROGRESS.getCode(), updatedActivity.getStatus());
    }

    @Test
    public void testGetActivityPage() {
        // 创建多个活动
        for (int i = 1; i <= 5; i++) {
            Activity activity = new Activity();
            activity.setTitle("测试活动" + i);
            activity.setDescription("测试活动描述" + i);
            activity.setStartTime(LocalDateTime.now().plusDays(i));
            activity.setEndTime(LocalDateTime.now().plusDays(i).plusHours(2));
            activity.setLocation("测试地点" + i);
            activity.setRequiredPeople(10 + i);
            activity.setCategory(ActivityCategoryEnum.EDUCATION.getCode());
            activity.setOrganizerId(1L);
            activityService.save(activity);
        }

        // 分页查询
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<Activity> page = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(1, 3);
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<Activity> result = activityService.getActivityPage(page, null, null, null);

        // 验证分页结果
        assertNotNull(result);
        assertEquals(3, result.getRecords().size());
        assertTrue(result.getTotal() >= 5);
    }
}