package com.rosy.volunteer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.domain.entity.PageRequest;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.PageUtils;
import com.rosy.volunteer.domain.dto.RegistrationCreateDTO;
import com.rosy.volunteer.domain.dto.RegistrationReviewDTO;
import com.rosy.volunteer.domain.entity.Activity;
import com.rosy.volunteer.domain.entity.ActivityRegistration;
import com.rosy.volunteer.domain.vo.RegistrationDetailVO;
import com.rosy.volunteer.domain.vo.RegistrationListVO;
import com.rosy.volunteer.enums.ActivityStatusEnum;
import com.rosy.volunteer.enums.RegistrationStatusEnum;
import com.rosy.volunteer.mapper.ActivityMapper;
import com.rosy.volunteer.mapper.ActivityRegistrationMapper;
import com.rosy.volunteer.service.IRegistrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements IRegistrationService {

    private final ActivityRegistrationMapper registrationMapper;
    private final ActivityMapper activityMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createRegistration(RegistrationCreateDTO dto) {
        Activity activity = activityMapper.selectById(dto.getActivityId());
        if (activity == null) {
            throw new BusinessException("活动不存在");
        }
        if (!activity.getStatus().equals(ActivityStatusEnum.RECRUITING.getCode())) {
            throw new BusinessException("活动不在招募中，无法报名");
        }
        if (activity.getRegisteredCount() >= activity.getRequiredVolunteers()) {
            throw new BusinessException("活动名额已满");
        }
        LambdaQueryWrapper<ActivityRegistration> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ActivityRegistration::getActivityId, dto.getActivityId())
               .eq(ActivityRegistration::getVolunteerId, 1L);
        if (registrationMapper.selectCount(wrapper) > 0) {
            throw new BusinessException("您已报名过此活动");
        }
        ActivityRegistration registration = new ActivityRegistration();
        BeanUtils.copyProperties(dto, registration);
        registration.setVolunteerId(1L);
        registration.setStatus(RegistrationStatusEnum.PENDING.getCode());
        registration.setCreatorId(1L);
        registration.setCreateTime(LocalDateTime.now());
        registrationMapper.insert(registration);
        activity.setRegisteredCount(activity.getRegisteredCount() + 1);
        activityMapper.updateById(activity);
        autoReviewRegistration(registration.getId());
        return registration.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelRegistration(Long id) {
        ActivityRegistration registration = getRegistrationById(id);
        if (registration.getStatus().equals(RegistrationStatusEnum.APPROVED.getCode())) {
            throw new BusinessException("已通过审核的报名不能取消");
        }
        registration.setStatus(RegistrationStatusEnum.CANCELLED.getCode());
        registration.setUpdaterId(1L);
        registration.setUpdateTime(LocalDateTime.now());
        registrationMapper.updateById(registration);
        Activity activity = activityMapper.selectById(registration.getActivityId());
        activity.setRegisteredCount(activity.getRegisteredCount() - 1);
        activityMapper.updateById(activity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reviewRegistration(Long id, RegistrationReviewDTO dto) {
        ActivityRegistration registration = getRegistrationById(id);
        if (!registration.getStatus().equals(RegistrationStatusEnum.PENDING.getCode())) {
            throw new BusinessException("报名不在待审核状态");
        }
        registration.setStatus(dto.getStatus());
        registration.setReviewerId(1L);
        registration.setReviewTime(LocalDateTime.now());
        registration.setReviewNote(dto.getReviewNote());
        registration.setUpdaterId(1L);
        registration.setUpdateTime(LocalDateTime.now());
        registrationMapper.updateById(registration);
    }

    @Override
    public void autoReviewRegistration(Long id) {
        ActivityRegistration registration = getRegistrationById(id);
        Activity activity = activityMapper.selectById(registration.getActivityId());
        boolean autoApprove = true;
        if (autoApprove) {
            registration.setStatus(RegistrationStatusEnum.APPROVED.getCode());
            registration.setReviewerId(0L);
            registration.setReviewTime(LocalDateTime.now());
            registration.setReviewNote("自动审核通过");
            registrationMapper.updateById(registration);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void checkIn(Long id) {
        ActivityRegistration registration = getRegistrationById(id);
        if (!registration.getStatus().equals(RegistrationStatusEnum.APPROVED.getCode())) {
            throw new BusinessException("报名未通过审核，无法签到");
        }
        if (registration.getIsCheckedIn() == 1) {
            throw new BusinessException("已签到，无需重复签到");
        }
        Activity activity = activityMapper.selectById(registration.getActivityId());
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(activity.getActivityTime().minusHours(2))) {
            throw new BusinessException("签到时间过早，活动开始前2小时才能签到");
        }
        if (now.isAfter(activity.getActivityTime().plusMinutes(30))) {
            throw new BusinessException("签到时间已过，活动开始30分钟后无法签到");
        }
        registration.setIsCheckedIn(1);
        registration.setCheckInTime(LocalDateTime.now());
        registration.setUpdaterId(1L);
        registration.setUpdateTime(LocalDateTime.now());
        registrationMapper.updateById(registration);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void checkOut(Long id) {
        ActivityRegistration registration = getRegistrationById(id);
        if (registration.getIsCheckedIn() != 1) {
            throw new BusinessException("未签到，无法签出");
        }
        if (registration.getIsCheckedOut() == 1) {
            throw new BusinessException("已签出，无需重复签出");
        }
        registration.setIsCheckedOut(1);
        registration.setCheckOutTime(LocalDateTime.now());
        registration.setUpdaterId(1L);
        registration.setUpdateTime(LocalDateTime.now());
        registrationMapper.updateById(registration);
    }

    @Override
    public RegistrationDetailVO getRegistrationDetail(Long id) {
        ActivityRegistration registration = getRegistrationById(id);
        RegistrationDetailVO vo = new RegistrationDetailVO();
        BeanUtils.copyProperties(registration, vo);
        return vo;
    }

    @Override
    public List<RegistrationListVO> getRegistrationList(Long activityId, Integer status, Long volunteerId) {
        LambdaQueryWrapper<ActivityRegistration> wrapper = new LambdaQueryWrapper<>();
        if (activityId != null) {
            wrapper.eq(ActivityRegistration::getActivityId, activityId);
        }
        if (status != null) {
            wrapper.eq(ActivityRegistration::getStatus, status);
        }
        if (volunteerId != null) {
            wrapper.eq(ActivityRegistration::getVolunteerId, volunteerId);
        }
        wrapper.orderByDesc("create_time");
        List<ActivityRegistration> registrations = registrationMapper.selectList(wrapper);
        return convertToListVO(registrations);
    }

    @Override
    public Object getRegistrationPage(Long activityId, Integer status, Long volunteerId, PageRequest pageRequest) {
        LambdaQueryWrapper<ActivityRegistration> wrapper = new LambdaQueryWrapper<>();
        if (activityId != null) {
            wrapper.eq(ActivityRegistration::getActivityId, activityId);
        }
        if (status != null) {
            wrapper.eq(ActivityRegistration::getStatus, status);
        }
        if (volunteerId != null) {
            wrapper.eq(ActivityRegistration::getVolunteerId, volunteerId);
        }
        wrapper.orderByDesc("create_time");
        Page<ActivityRegistration> page = new Page<>(pageRequest.getPageNum(), pageRequest.getPageSize());
        Page<ActivityRegistration> result = registrationMapper.selectPage(page, wrapper);
        return PageUtils.buildPageResult(result, this::convertToListVO);
    }

    @Override
    public void sendActivityReminder(Long activityId) {
        log.info("发送活动提醒: activityId={}", activityId);
    }

    private ActivityRegistration getRegistrationById(Long id) {
        ActivityRegistration registration = registrationMapper.selectById(id);
        if (registration == null) {
            throw new BusinessException("报名记录不存在");
        }
        return registration;
    }

    private List<RegistrationListVO> convertToListVO(List<ActivityRegistration> registrations) {
        return registrations.stream().map(registration -> {
            RegistrationListVO vo = new RegistrationListVO();
            BeanUtils.copyProperties(registration, vo);
            RegistrationStatusEnum statusEnum = RegistrationStatusEnum.getByCode(registration.getStatus());
            if (statusEnum != null) {
                vo.setStatusName(statusEnum.getDesc());
            }
            return vo;
        }).toList();
    }
}
