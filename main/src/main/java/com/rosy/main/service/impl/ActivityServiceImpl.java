package com.rosy.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.main.domain.entity.Activity;
import com.rosy.main.mapper.ActivityMapper;
import com.rosy.main.service.IActivityService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * 活动Service实现类
 */
@Service
public class ActivityServiceImpl extends ServiceImpl<ActivityMapper, Activity> implements IActivityService {

    @Override
    public Page<Activity> getActivityPage(Page<Activity> page, String title, Integer category, 
                                        Integer status, String location, Long creatorId) {
        QueryWrapper<Activity> queryWrapper = new QueryWrapper<>();
        
        // 添加查询条件
        if (StringUtils.isNotBlank(title)) {
            queryWrapper.like("title", title);
        }
        if (category != null) {
            queryWrapper.eq("category", category);
        }
        if (status != null) {
            queryWrapper.eq("status", status);
        }
        if (StringUtils.isNotBlank(location)) {
            queryWrapper.like("location", location);
        }
        if (creatorId != null) {
            queryWrapper.eq("creator_id", creatorId);
        }
        
        // 按创建时间倒序排列
        queryWrapper.orderByDesc("create_time");
        
        return this.page(page, queryWrapper);
    }

    @Override
    public boolean updateActivityStatus(Long activityId, Integer status) {
        Activity activity = new Activity();
        activity.setId(activityId);
        activity.setStatus(status);
        return this.updateById(activity);
    }

    @Override
    public boolean increaseCurrentCount(Long activityId) {
        Activity activity = this.getById(activityId);
        if (activity == null) {
            return false;
        }
        
        // 检查是否还有名额
        if (activity.getCurrentCount() >= activity.getRequiredCount()) {
            return false;
        }
        
        activity.setCurrentCount(activity.getCurrentCount() + 1);
        return this.updateById(activity);
    }

    @Override
    public boolean decreaseCurrentCount(Long activityId) {
        Activity activity = this.getById(activityId);
        if (activity == null || activity.getCurrentCount() <= 0) {
            return false;
        }
        
        activity.setCurrentCount(activity.getCurrentCount() - 1);
        return this.updateById(activity);
    }
}