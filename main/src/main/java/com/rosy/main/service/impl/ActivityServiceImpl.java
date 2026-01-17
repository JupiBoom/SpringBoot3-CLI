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

/**
 * 活动服务实现类
 *
 * @author Rosy
 * @since 2026-01-17
 */
@Service
public class ActivityServiceImpl extends ServiceImpl<ActivityMapper, Activity> implements IActivityService {

    @Override
    public ActivityVO getActivityVO(Activity activity) {
        return Optional.ofNullable(activity)
                .map(a -> BeanUtil.copyProperties(a, ActivityVO.class))
                .orElse(null);
    }

    @Override
    public LambdaQueryWrapper<Activity> getQueryWrapper(ActivityQueryRequest activityQueryRequest) {
        if (activityQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        LambdaQueryWrapper<Activity> queryWrapper = new LambdaQueryWrapper<>();

        // 动态添加条件
        QueryWrapperUtil.addCondition(queryWrapper, activityQueryRequest.getId(), Activity::getId);
        QueryWrapperUtil.addCondition(queryWrapper, activityQueryRequest.getName(), Activity::getName);
        QueryWrapperUtil.addCondition(queryWrapper, activityQueryRequest.getDescription(), Activity::getDescription);
        QueryWrapperUtil.addCondition(queryWrapper, activityQueryRequest.getLocation(), Activity::getLocation);
        QueryWrapperUtil.addCondition(queryWrapper, activityQueryRequest.getCategory(), Activity::getCategory);
        QueryWrapperUtil.addCondition(queryWrapper, activityQueryRequest.getStatus(), Activity::getStatus);
        QueryWrapperUtil.addCondition(queryWrapper, activityQueryRequest.getCreatorId(), Activity::getCreatorId);
        QueryWrapperUtil.addCondition(queryWrapper, activityQueryRequest.getCreateTime(), Activity::getCreateTime);

        // 添加排序条件
        QueryWrapperUtil.addSortCondition(queryWrapper,
                activityQueryRequest.getSortField(),
                activityQueryRequest.getSortOrder(),
                Activity::getId);

        return queryWrapper;
    }
}