package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.QueryWrapperUtil;
import com.rosy.main.domain.dto.activity.ActivityQueryRequest;
import com.rosy.main.domain.entity.Activity;
import com.rosy.main.domain.vo.ActivityVO;
import com.rosy.main.mapper.ActivityMapper;
import com.rosy.main.service.IActivityService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ActivityServiceImpl extends ServiceImpl<ActivityMapper, Activity> implements IActivityService {

    @Override
    public ActivityVO getActivityVO(Activity activity) {
        if (activity == null) {
            return null;
        }
        ActivityVO activityVO = BeanUtil.copyProperties(activity, ActivityVO.class);
        activityVO.setCategoryName(getCategoryName(activity.getCategory()));
        activityVO.setStatusName(getStatusName(activity.getStatus()));
        return activityVO;
    }

    @Override
    public LambdaQueryWrapper<Activity> getQueryWrapper(ActivityQueryRequest activityQueryRequest) {
        if (activityQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        LambdaQueryWrapper<Activity> queryWrapper = new LambdaQueryWrapper<>();

        QueryWrapperUtil.addCondition(queryWrapper, activityQueryRequest.getId(), Activity::getId);
        QueryWrapperUtil.addCondition(queryWrapper, activityQueryRequest.getTitle(), Activity::getTitle);
        QueryWrapperUtil.addCondition(queryWrapper, activityQueryRequest.getCategory(), Activity::getCategory);
        QueryWrapperUtil.addCondition(queryWrapper, activityQueryRequest.getStatus(), Activity::getStatus);
        QueryWrapperUtil.addCondition(queryWrapper, activityQueryRequest.getStartTime(), Activity::getStartTime);
        QueryWrapperUtil.addCondition(queryWrapper, activityQueryRequest.getEndTime(), Activity::getEndTime);
        QueryWrapperUtil.addCondition(queryWrapper, activityQueryRequest.getLocation(), Activity::getLocation);
        QueryWrapperUtil.addCondition(queryWrapper, activityQueryRequest.getCreatorId(), Activity::getCreatorId);

        QueryWrapperUtil.addSortCondition(queryWrapper,
                activityQueryRequest.getSortField(),
                activityQueryRequest.getSortOrder(),
                Activity::getId);

        return queryWrapper;
    }

    @Override
    public boolean updateActivityStatus(Long activityId, Byte status) {
        Activity activity = new Activity();
        activity.setId(activityId);
        activity.setStatus(status);
        return updateById(activity);
    }

    @Override
    public boolean incrementCurrentPeople(Long activityId) {
        Activity activity = getById(activityId);
        if (activity == null) {
            return false;
        }
        activity.setCurrentPeople(activity.getCurrentPeople() + 1);
        return updateById(activity);
    }

    @Override
    public boolean decrementCurrentPeople(Long activityId) {
        Activity activity = getById(activityId);
        if (activity == null || activity.getCurrentPeople() <= 0) {
            return false;
        }
        activity.setCurrentPeople(activity.getCurrentPeople() - 1);
        return updateById(activity);
    }

    private String getCategoryName(Byte category) {
        if (category == null) {
            return "";
        }
        return switch (category) {
            case 1 -> "环保";
            case 2 -> "助老";
            case 3 -> "教育";
            case 4 -> "医疗";
            default -> "";
        };
    }

    private String getStatusName(Byte status) {
        if (status == null) {
            return "";
        }
        return switch (status) {
            case 0 -> "招募中";
            case 1 -> "进行中";
            case 2 -> "已完成";
            default -> "";
        };
    }
}
