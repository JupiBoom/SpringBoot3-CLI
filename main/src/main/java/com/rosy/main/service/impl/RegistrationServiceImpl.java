package com.rosy.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.entity.Activity;
import com.rosy.main.domain.entity.Registration;
import com.rosy.main.mapper.RegistrationMapper;
import com.rosy.main.service.IActivityService;
import com.rosy.main.service.IRegistrationService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class RegistrationServiceImpl extends ServiceImpl<RegistrationMapper, Registration> implements IRegistrationService {

    @Resource
    private IActivityService activityService;

    @Override
    public LambdaQueryWrapper<Registration> getQueryWrapper(Long activityId, Long userId, Byte status) {
        LambdaQueryWrapper<Registration> queryWrapper = new LambdaQueryWrapper<>();
        if (activityId != null) {
            queryWrapper.eq(Registration::getActivityId, activityId);
        }
        if (userId != null) {
            queryWrapper.eq(Registration::getUserId, userId);
        }
        if (status != null) {
            queryWrapper.eq(Registration::getStatus, status);
        }
        queryWrapper.orderByDesc(Registration::getCreateTime);
        return queryWrapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean registerActivity(Long activityId, Long userId, Byte auditType) {
        Activity activity = activityService.getById(activityId);
        if (activity == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "活动不存在");
        }
        if (activity.getStatus() != 0) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "活动不在招募中");
        }
        if (activity.getCurrentPeople() >= activity.getRequiredPeople()) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "活动已满员");
        }
        if (checkDuplicateRegistration(activityId, userId)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "已报名该活动");
        }

        Registration registration = new Registration();
        registration.setActivityId(activityId);
        registration.setUserId(userId);
        registration.setAuditType(auditType);

        if (auditType == 1) {
            registration.setStatus((byte) 1);
            registration.setAuditTime(LocalDateTime.now());
            activityService.incrementCurrentPeople(activityId);
        } else {
            registration.setStatus((byte) 0);
        }

        return save(registration);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean auditRegistration(Long registrationId, Long auditorId, Byte status, String remark) {
        Registration registration = getById(registrationId);
        if (registration == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "报名记录不存在");
        }
        if (registration.getStatus() != 0) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "该报名已审核");
        }

        registration.setStatus(status);
        registration.setAuditTime(LocalDateTime.now());
        registration.setAuditorId(auditorId);
        registration.setAuditRemark(remark);

        if (status == 1) {
            activityService.incrementCurrentPeople(registration.getActivityId());
        }

        return updateById(registration);
    }

    @Override
    public boolean checkDuplicateRegistration(Long activityId, Long userId) {
        LambdaQueryWrapper<Registration> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Registration::getActivityId, activityId)
                .eq(Registration::getUserId, userId)
                .ne(Registration::getStatus, (byte) 2);
        return count(wrapper) > 0;
    }
}
