package com.rosy.main.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.ActivityPhoto;

public interface IActivityPhotoService extends IService<ActivityPhoto> {

    LambdaQueryWrapper<ActivityPhoto> getQueryWrapper(Long activityId, Long forumPostId);
}
