package com.rosy.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.main.domain.entity.ActivityPhoto;
import com.rosy.main.mapper.ActivityPhotoMapper;
import com.rosy.main.service.IActivityPhotoService;
import org.springframework.stereotype.Service;

@Service
public class ActivityPhotoServiceImpl extends ServiceImpl<ActivityPhotoMapper, ActivityPhoto> implements IActivityPhotoService {

    @Override
    public LambdaQueryWrapper<ActivityPhoto> getQueryWrapper(Long activityId, Long forumPostId) {
        LambdaQueryWrapper<ActivityPhoto> queryWrapper = new LambdaQueryWrapper<>();
        if (activityId != null) {
            queryWrapper.eq(ActivityPhoto::getActivityId, activityId);
        }
        if (forumPostId != null) {
            queryWrapper.eq(ActivityPhoto::getForumPostId, forumPostId);
        }
        queryWrapper.orderByDesc(ActivityPhoto::getCreateTime);
        return queryWrapper;
    }
}
