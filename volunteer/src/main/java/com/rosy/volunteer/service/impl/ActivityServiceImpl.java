package com.rosy.volunteer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.domain.entity.PageRequest;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.PageUtils;
import com.rosy.volunteer.domain.dto.ActivityCreateDTO;
import com.rosy.volunteer.domain.dto.ActivityUpdateDTO;
import com.rosy.volunteer.domain.entity.Activity;
import com.rosy.volunteer.domain.vo.ActivityDetailVO;
import com.rosy.volunteer.domain.vo.ActivityListVO;
import com.rosy.volunteer.enums.ActivityStatusEnum;
import com.rosy.volunteer.mapper.ActivityMapper;
import com.rosy.volunteer.service.IActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements IActivityService {

    private final ActivityMapper activityMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createActivity(ActivityCreateDTO dto) {
        Activity activity = new Activity();
        BeanUtils.copyProperties(dto, activity);
        activity.setRegisteredCount(0);
        activity.setStatus(ActivityStatusEnum.RECRUITING.getCode());
        activity.setCreatorId(1L);
        activity.setCreateTime(LocalDateTime.now());
        activityMapper.insert(activity);
        return activity.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateActivity(Long id, ActivityUpdateDTO dto) {
        Activity activity = getActivityById(id);
        if (activity.getStatus().equals(ActivityStatusEnum.COMPLETED.getCode())) {
            throw new BusinessException("已完成的活动不能修改");
        }
        if (StringUtils.hasText(dto.getTitle())) {
            activity.setTitle(dto.getTitle());
        }
        if (StringUtils.hasText(dto.getDescription())) {
            activity.setDescription(dto.getDescription());
        }
        if (dto.getCategoryId() != null) {
            activity.setCategoryId(dto.getCategoryId());
        }
        if (dto.getActivityTime() != null) {
            activity.setActivityTime(dto.getActivityTime());
        }
        if (StringUtils.hasText(dto.getLocation())) {
            activity.setLocation(dto.getLocation());
        }
        if (dto.getRequiredVolunteers() != null) {
            activity.setRequiredVolunteers(dto.getRequiredVolunteers());
        }
        if (StringUtils.hasText(dto.getContactName())) {
            activity.setContactName(dto.getContactName());
        }
        if (StringUtils.hasText(dto.getContactPhone())) {
            activity.setContactPhone(dto.getContactPhone());
        }
        if (StringUtils.hasText(dto.getCoverImage())) {
            activity.setCoverImage(dto.getCoverImage());
        }
        if (dto.getMaxDuration() != null) {
            activity.setMaxDuration(dto.getMaxDuration());
        }
        activity.setUpdaterId(1L);
        activity.setUpdateTime(LocalDateTime.now());
        activityMapper.updateById(activity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteActivity(Long id) {
        Activity activity = getActivityById(id);
        if (activity.getStatus().equals(ActivityStatusEnum.IN_PROGRESS.getCode())) {
            throw new BusinessException("进行中的活动不能删除");
        }
        activityMapper.deleteById(id);
    }

    @Override
    public ActivityDetailVO getActivityDetail(Long id) {
        Activity activity = getActivityById(id);
        ActivityDetailVO vo = new ActivityDetailVO();
        BeanUtils.copyProperties(activity, vo);
        ActivityStatusEnum statusEnum = ActivityStatusEnum.getByCode(activity.getStatus());
        if (statusEnum != null) {
            vo.setStatusName(statusEnum.getDesc());
        }
        return vo;
    }

    @Override
    public List<ActivityListVO> getActivityList(Long categoryId, Integer status, String keyword) {
        LambdaQueryWrapper<Activity> wrapper = buildQueryWrapper(categoryId, status, keyword);
        wrapper.orderByDesc("activity_time");
        List<Activity> activities = activityMapper.selectList(wrapper);
        return convertToListVO(activities);
    }

    @Override
    public Object getActivityPage(Long categoryId, Integer status, String keyword, PageRequest pageRequest) {
        LambdaQueryWrapper<Activity> wrapper = buildQueryWrapper(categoryId, status, keyword);
        wrapper.orderByDesc("activity_time");
        Page<Activity> page = new Page<>(pageRequest.getPageNum(), pageRequest.getPageSize());
        Page<Activity> result = activityMapper.selectPage(page, wrapper);
        return PageUtils.buildPageResult(result, this::convertToListVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateActivityStatus(Long id, Integer status) {
        Activity activity = getActivityById(id);
        if (ActivityStatusEnum.getByCode(status) == null) {
            throw new BusinessException("无效的活动状态");
        }
        activity.setStatus(status);
        activity.setUpdaterId(1L);
        activity.setUpdateTime(LocalDateTime.now());
        activityMapper.updateById(activity);
    }

    @Override
    public List<ActivityListVO> getHotActivities(Integer limit) {
        LambdaQueryWrapper<Activity> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc("registered_count").last("LIMIT " + limit);
        List<Activity> activities = activityMapper.selectList(wrapper);
        return convertToListVO(activities);
    }

    @Override
    public List<ActivityListVO> getUpcomingActivities(Integer limit) {
        LambdaQueryWrapper<Activity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Activity::getStatus, ActivityStatusEnum.RECRUITING.getCode())
                .gt(Activity::getActivityTime, LocalDateTime.now())
                .orderByAsc("activity_time").last("LIMIT " + limit);
        List<Activity> activities = activityMapper.selectList(wrapper);
        return convertToListVO(activities);
    }

    private Activity getActivityById(Long id) {
        Activity activity = activityMapper.selectById(id);
        if (activity == null) {
            throw new BusinessException("活动不存在");
        }
        return activity;
    }

    private LambdaQueryWrapper<Activity> buildQueryWrapper(Long categoryId, Integer status, String keyword) {
        LambdaQueryWrapper<Activity> wrapper = new LambdaQueryWrapper<>();
        if (categoryId != null) {
            wrapper.eq(Activity::getCategoryId, categoryId);
        }
        if (status != null) {
            wrapper.eq(Activity::getStatus, status);
        }
        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w.like(Activity::getTitle, keyword)
                    .or().like(Activity::getDescription, keyword)
                    .or().like(Activity::getLocation, keyword));
        }
        return wrapper;
    }

    private List<ActivityListVO> convertToListVO(List<Activity> activities) {
        return activities.stream().map(activity -> {
            ActivityListVO vo = new ActivityListVO();
            BeanUtils.copyProperties(activity, vo);
            ActivityStatusEnum statusEnum = ActivityStatusEnum.getByCode(activity.getStatus());
            if (statusEnum != null) {
                vo.setStatusName(statusEnum.getDesc());
            }
            return vo;
        }).collect(Collectors.toList());
    }
}
