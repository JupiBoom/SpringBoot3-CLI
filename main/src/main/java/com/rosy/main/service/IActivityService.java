package com.rosy.main.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.activity.ActivityQueryRequest;
import com.rosy.main.domain.entity.Activity;
import com.rosy.main.domain.vo.ActivityVO;

/**
 * 活动服务接口
 *
 * @author Rosy
 * @since 2026-01-17
 */
public interface IActivityService extends IService<Activity> {

    ActivityVO getActivityVO(Activity activity);

    LambdaQueryWrapper<Activity> getQueryWrapper(ActivityQueryRequest activityQueryRequest);
}