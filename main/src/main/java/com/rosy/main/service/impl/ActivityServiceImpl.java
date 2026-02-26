package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ActivityCategoryEnum;
import com.rosy.common.enums.ActivityStatusEnum;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.QueryWrapperUtil;
import com.rosy.main.domain.dto.activity.ActivityQueryRequest;
import com.rosy.main.domain.entity.Activity;
import com.rosy.main.domain.entity.User;
import com.rosy.main.domain.vo.ActivityVO;
import com.rosy.main.mapper.ActivityMapper;
import com.rosy.main.mapper.UserMapper;
import com.rosy.main.service.IActivityService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ActivityServiceImpl extends ServiceImpl<ActivityMapper, Activity> implements IActivityService {

    @Resource
    private UserMapper userMapper;

    @Override
    public ActivityVO getActivityVO(Activity activity) {
        if (activity == null) {
            return null;
        }
        ActivityVO vo = BeanUtil.copyProperties(activity, ActivityVO.class);
        
        ActivityCategoryEnum categoryEnum = ActivityCategoryEnum.getByCode(activity.getCategory());
        if (categoryEnum != null) {
            vo.setCategoryDesc(categoryEnum.getDesc());
        }
        
        ActivityStatusEnum statusEnum = ActivityStatusEnum.getByCode(activity.getStatus());
        if (statusEnum != null) {
            vo.setStatusDesc(statusEnum.getDesc());
        }
        
        if (activity.getRequiredCount() != null && activity.getCurrentCount() != null) {
            vo.setRemainingCount(activity.getRequiredCount() - activity.getCurrentCount());
        }
        
        if (activity.getOrganizerId() != null) {
            User organizer = userMapper.selectById(activity.getOrganizerId());
            if (organizer != null) {
                vo.setOrganizerName(organizer.getRealName() != null ? organizer.getRealName() : organizer.getUsername());
            }
        }
        
        return vo;
    }

    @Override
    public LambdaQueryWrapper<Activity> getQueryWrapper(ActivityQueryRequest queryRequest) {
        if (queryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        LambdaQueryWrapper<Activity> queryWrapper = new LambdaQueryWrapper<>();

        QueryWrapperUtil.addCondition(queryWrapper, queryRequest.getId(), Activity::getId);
        QueryWrapperUtil.addLikeCondition(queryWrapper, queryRequest.getTitle(), Activity::getTitle);
        QueryWrapperUtil.addCondition(queryWrapper, queryRequest.getCategory(), Activity::getCategory);
        QueryWrapperUtil.addCondition(queryWrapper, queryRequest.getStatus(), Activity::getStatus);
        QueryWrapperUtil.addCondition(queryWrapper, queryRequest.getOrganizerId(), Activity::getOrganizerId);
        QueryWrapperUtil.addLikeCondition(queryWrapper, queryRequest.getLocation(), Activity::getLocation);

        if (queryRequest.getStartTimeBegin() != null) {
            queryWrapper.ge(Activity::getStartTime, queryRequest.getStartTimeBegin());
        }
        if (queryRequest.getStartTimeEnd() != null) {
            queryWrapper.le(Activity::getStartTime, queryRequest.getStartTimeEnd());
        }

        QueryWrapperUtil.addSortCondition(queryWrapper,
                queryRequest.getSortField(),
                queryRequest.getSortOrder(),
                Activity::getCreateTime);

        return queryWrapper;
    }
}
