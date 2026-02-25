package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ActivityStatusEnum;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.dto.ActivityDTO;
import com.rosy.main.domain.dto.ActivityQueryDTO;
import com.rosy.main.domain.entity.Activity;
import com.rosy.main.mapper.ActivityMapper;
import com.rosy.main.service.IActivityService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Service
public class ActivityServiceImpl extends ServiceImpl<ActivityMapper, Activity> implements IActivityService {

    @Override
    public Activity create(ActivityDTO dto, Long creatorId) {
        Activity activity = BeanUtil.copyProperties(dto, Activity.class);
        activity.setCreatorId(creatorId);
        activity.setAppliedPeople(0);
        
        LocalDateTime now = LocalDateTime.now();
        if (dto.getStartTime().isBefore(now)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "活动开始时间不能早于当前时间");
        }
        if (dto.getEndTime().isBefore(dto.getStartTime())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "活动结束时间不能早于开始时间");
        }
        
        if (dto.getStartTime().isAfter(now)) {
            activity.setStatus(ActivityStatusEnum.RECRUITING.name());
        } else {
            activity.setStatus(ActivityStatusEnum.ONGOING.name());
        }
        
        save(activity);
        return activity;
    }

    @Override
    public Activity update(Long id, ActivityDTO dto) {
        Activity activity = getById(id);
        if (activity == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "活动不存在");
        }
        if (!ActivityStatusEnum.RECRUITING.name().equals(activity.getStatus())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "只能修改招募中的活动");
        }
        
        BeanUtil.copyProperties(dto, activity, "id", "creatorId", "appliedPeople", "status", "createTime");
        updateById(activity);
        return activity;
    }

    @Override
    public boolean delete(Long id) {
        Activity activity = getById(id);
        if (activity == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "活动不存在");
        }
        if (activity.getAppliedPeople() > 0) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "已有志愿者报名，无法删除");
        }
        return removeById(id);
    }

    @Override
    public Page<Activity> pageQuery(ActivityQueryDTO queryDTO) {
        Page<Activity> page = new Page<>(queryDTO.getCurrent(), queryDTO.getPageSize());
        LambdaQueryWrapper<Activity> wrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(queryDTO.getKeyword())) {
            wrapper.and(w -> w.like(Activity::getTitle, queryDTO.getKeyword())
                    .or().like(Activity::getDescription, queryDTO.getKeyword())
                    .or().like(Activity::getLocation, queryDTO.getKeyword()));
        }
        if (StringUtils.hasText(queryDTO.getCategory())) {
            wrapper.eq(Activity::getCategory, queryDTO.getCategory());
        }
        if (StringUtils.hasText(queryDTO.getStatus())) {
            wrapper.eq(Activity::getStatus, queryDTO.getStatus());
        }
        
        wrapper.eq(Activity::getIsDeleted, 0)
               .orderByDesc(Activity::getCreateTime);
        
        return page(page, wrapper);
    }

    @Override
    public boolean updateStatus(Long id, String status) {
        Activity activity = getById(id);
        if (activity == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "活动不存在");
        }
        activity.setStatus(status);
        return updateById(activity);
    }
}
