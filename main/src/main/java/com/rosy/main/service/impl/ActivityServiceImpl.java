package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ActivityCategoryEnum;
import com.rosy.common.enums.ActivityStatusEnum;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.dto.ActivityDTO;
import com.rosy.main.domain.entity.Activity;
import com.rosy.main.domain.vo.ActivityVO;
import com.rosy.main.mapper.ActivityMapper;
import com.rosy.main.service.IActivityService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ActivityServiceImpl extends ServiceImpl<ActivityMapper, Activity> implements IActivityService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createActivity(ActivityDTO dto, Long userId) {
        if (dto.getEndTime().isBefore(dto.getStartTime())) {
            throw new BusinessException("结束时间必须晚于开始时间");
        }
        
        Activity activity = BeanUtil.copyProperties(dto, Activity.class);
        activity.setCreatorId(userId);
        activity.setRegisteredNum(0);
        activity.setStatus(ActivityStatusEnum.RECRUITING.getValue());
        
        save(activity);
        return activity.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateActivity(ActivityDTO dto, Long userId) {
        Activity existing = getById(dto.getId());
        if (existing == null) {
            throw new BusinessException("活动不存在");
        }
        
        if (!existing.getCreatorId().equals(userId)) {
            throw new BusinessException("无权修改此活动");
        }
        
        if (dto.getEndTime().isBefore(dto.getStartTime())) {
            throw new BusinessException("结束时间必须晚于开始时间");
        }
        
        BeanUtil.copyProperties(dto, existing, "id", "creatorId", "registeredNum", "status", "createTime");
        existing.setUpdaterId(userId);
        
        updateById(existing);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteActivity(Long id) {
        Activity activity = getById(id);
        if (activity == null) {
            throw new BusinessException("活动不存在");
        }
        removeById(id);
    }

    @Override
    public ActivityVO getActivityDetail(Long id) {
        ActivityVO vo = baseMapper.selectActivityDetail(id);
        if (vo == null) {
            throw new BusinessException("活动不存在");
        }
        vo.setCategoryName(ActivityCategoryEnum.getTextByValue(vo.getCategory()));
        vo.setStatusName(ActivityStatusEnum.getTextByValue(vo.getStatus()));
        return vo;
    }

    @Override
    public Page<ActivityVO> getActivityPage(Integer page, Integer size, Integer category, Integer status, String keyword) {
        Page<Activity> pageParam = new Page<>(page, size);
        
        LambdaQueryWrapper<Activity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(category != null, Activity::getCategory, category)
               .eq(status != null, Activity::getStatus, status)
               .like(StringUtils.isNotBlank(keyword), Activity::getTitle, keyword)
               .orderByDesc(Activity::getCreateTime);
        
        Page<Activity> activityPage = page(pageParam, wrapper);
        
        Page<ActivityVO> voPage = new Page<>(activityPage.getCurrent(), activityPage.getSize(), activityPage.getTotal());
        List<ActivityVO> list = activityPage.getRecords().stream().map(activity -> {
            ActivityVO vo = BeanUtil.copyProperties(activity, ActivityVO.class);
            vo.setCategoryName(ActivityCategoryEnum.getTextByValue(vo.getCategory()));
            vo.setStatusName(ActivityStatusEnum.getTextByValue(vo.getStatus()));
            return vo;
        }).toList();
        voPage.setRecords(list);
        
        return voPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateActivityStatus() {
        LocalDateTime now = LocalDateTime.now();
        List<Activity> activities = baseMapper.selectActivitiesToUpdateStatus(now);
        
        for (Activity activity : activities) {
            int newStatus = activity.getStatus();
            
            if (activity.getEndTime().isBefore(now)) {
                newStatus = ActivityStatusEnum.COMPLETED.getValue();
            } else if (activity.getStartTime().isBefore(now) && activity.getStatus().equals(ActivityStatusEnum.RECRUITING.getValue())) {
                newStatus = ActivityStatusEnum.ONGOING.getValue();
            }
            
            if (newStatus != activity.getStatus()) {
                activity.setStatus(newStatus);
                updateById(activity);
            }
        }
    }
}
