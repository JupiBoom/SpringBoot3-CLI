package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.dto.ActivityCreateRequest;
import com.rosy.main.domain.dto.ActivityQueryRequest;
import com.rosy.main.domain.dto.ActivityUpdateRequest;
import com.rosy.main.domain.entity.Activity;
import com.rosy.main.domain.enums.ActivityCategoryEnum;
import com.rosy.main.domain.enums.ActivityStatusEnum;
import com.rosy.main.domain.vo.ActivityVO;
import com.rosy.main.mapper.ActivityMapper;
import com.rosy.main.service.IActivityService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 活动服务实现类
 */
@Service
public class ActivityServiceImpl extends ServiceImpl<ActivityMapper, Activity> implements IActivityService {

    @Override
    public Long createActivity(ActivityCreateRequest request, Long organizerId) {
        // 校验时间
        validateActivityTime(request.getStartTime(), request.getEndTime(),
                request.getRegistrationStart(), request.getRegistrationEnd());

        Activity activity = new Activity();
        BeanUtil.copyProperties(request, activity);
        activity.setOrganizerId(organizerId);
        activity.setStatus(ActivityStatusEnum.RECRUITING.getCode());
        activity.setRegisteredPeople(0);
        activity.setConfirmedPeople(0);
        activity.setViewCount(0);

        boolean saved = this.save(activity);
        if (!saved) {
            throw new BusinessException("创建活动失败");
        }
        return activity.getId();
    }

    @Override
    public boolean updateActivity(ActivityUpdateRequest request) {
        Activity oldActivity = this.getById(request.getId());
        if (oldActivity == null) {
            throw new BusinessException("活动不存在");
        }

        // 已结束或已取消的活动不能修改
        if (oldActivity.getStatus() == ActivityStatusEnum.COMPLETED.getCode() ||
                oldActivity.getStatus() == ActivityStatusEnum.CANCELLED.getCode()) {
            throw new BusinessException("已结束或已取消的活动不能修改");
        }

        Activity activity = new Activity();
        BeanUtil.copyProperties(request, activity);

        // 如果修改了时间，需要校验
        if (request.getStartTime() != null || request.getEndTime() != null) {
            LocalDateTime startTime = request.getStartTime() != null ? request.getStartTime() : oldActivity.getStartTime();
            LocalDateTime endTime = request.getEndTime() != null ? request.getEndTime() : oldActivity.getEndTime();
            LocalDateTime regStart = request.getRegistrationStart() != null ? request.getRegistrationStart() : oldActivity.getRegistrationStart();
            LocalDateTime regEnd = request.getRegistrationEnd() != null ? request.getRegistrationEnd() : oldActivity.getRegistrationEnd();
            validateActivityTime(startTime, endTime, regStart, regEnd);
        }

        return this.updateById(activity);
    }

    @Override
    public boolean deleteActivity(Long id) {
        Activity activity = this.getById(id);
        if (activity == null) {
            throw new BusinessException("活动不存在");
        }

        // 进行中的活动不能直接删除
        if (activity.getStatus() == ActivityStatusEnum.IN_PROGRESS.getCode()) {
            throw new BusinessException("进行中的活动不能删除，请先取消活动");
        }

        return this.removeById(id);
    }

    @Override
    public ActivityVO getActivityById(Long id) {
        Activity activity = this.getById(id);
        if (activity == null) {
            return null;
        }
        return convertToVO(activity);
    }

    @Override
    public Page<ActivityVO> listActivities(ActivityQueryRequest request) {
        QueryWrapper<Activity> queryWrapper = getQueryWrapper(request);
        Page<Activity> page = this.page(new Page<>(request.getCurrent(), request.getPageSize()), queryWrapper);

        List<ActivityVO> voList = page.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        Page<ActivityVO> voPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    public List<ActivityVO> listAllActivities(ActivityQueryRequest request) {
        QueryWrapper<Activity> queryWrapper = getQueryWrapper(request);
        List<Activity> list = this.list(queryWrapper);
        return list.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public void incrementViewCount(Long id) {
        baseMapper.incrementViewCount(id);
    }

    @Override
    public boolean cancelActivity(Long id) {
        Activity activity = this.getById(id);
        if (activity == null) {
            throw new BusinessException("活动不存在");
        }

        // 已完成的活动不能取消
        if (activity.getStatus() == ActivityStatusEnum.COMPLETED.getCode()) {
            throw new BusinessException("已完成的活动不能取消");
        }

        // 已取消的活动不能重复取消
        if (activity.getStatus() == ActivityStatusEnum.CANCELLED.getCode()) {
            throw new BusinessException("活动已取消");
        }

        activity.setStatus(ActivityStatusEnum.CANCELLED.getCode());
        return this.updateById(activity);
    }

    @Override
    public QueryWrapper<Activity> getQueryWrapper(ActivityQueryRequest request) {
        QueryWrapper<Activity> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");

        // 关键词搜索
        if (StrUtil.isNotBlank(request.getKeyword())) {
            queryWrapper.and(qw -> qw.like("title", request.getKeyword())
                    .or()
                    .like("description", request.getKeyword()));
        }

        // 分类筛选
        if (request.getCategory() != null) {
            queryWrapper.eq("category", request.getCategory());
        }

        // 状态筛选
        if (request.getStatus() != null) {
            queryWrapper.eq("status", request.getStatus());
        }

        // 组织者筛选
        if (request.getOrganizerId() != null) {
            queryWrapper.eq("organizer_id", request.getOrganizerId());
        }

        // 地点筛选
        if (StrUtil.isNotBlank(request.getLocation())) {
            queryWrapper.like("location", request.getLocation());
        }

        // 日期范围筛选
        if (request.getStartDate() != null) {
            LocalDateTime startOfDay = request.getStartDate().atStartOfDay();
            queryWrapper.ge("start_time", startOfDay);
        }
        if (request.getEndDate() != null) {
            LocalDateTime endOfDay = request.getEndDate().atTime(LocalTime.MAX);
            queryWrapper.le("end_time", endOfDay);
        }

        return queryWrapper;
    }

    /**
     * 校验活动时间
     */
    private void validateActivityTime(LocalDateTime startTime, LocalDateTime endTime,
                                      LocalDateTime registrationStart, LocalDateTime registrationEnd) {
        if (startTime.isAfter(endTime)) {
            throw new BusinessException("活动开始时间不能晚于结束时间");
        }

        if (registrationStart != null && registrationEnd != null) {
            if (registrationStart.isAfter(registrationEnd)) {
                throw new BusinessException("报名开始时间不能晚于报名结束时间");
            }
            if (registrationEnd.isAfter(startTime)) {
                throw new BusinessException("报名结束时间不能晚于活动开始时间");
            }
        }
    }

    /**
     * 转换为VO
     */
    private ActivityVO convertToVO(Activity activity) {
        ActivityVO vo = new ActivityVO();
        BeanUtil.copyProperties(activity, vo);
        vo.setCategoryDesc(ActivityCategoryEnum.getDescByCode(activity.getCategory()));
        vo.setStatusDesc(ActivityStatusEnum.getDescByCode(activity.getStatus()));
        return vo;
    }
}
