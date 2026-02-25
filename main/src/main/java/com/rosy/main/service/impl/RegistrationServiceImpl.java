package com.rosy.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ActivityStatusEnum;
import com.rosy.common.enums.AuditTypeEnum;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.enums.NotificationTypeEnum;
import com.rosy.common.enums.RegistrationStatusEnum;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.dto.RegistrationAuditDTO;
import com.rosy.main.domain.entity.Activity;
import com.rosy.main.domain.entity.Notification;
import com.rosy.main.domain.entity.Registration;
import com.rosy.main.mapper.RegistrationMapper;
import com.rosy.main.service.IActivityService;
import com.rosy.main.service.INotificationService;
import com.rosy.main.service.IRegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl extends ServiceImpl<RegistrationMapper, Registration> implements IRegistrationService {

    private final IActivityService activityService;
    private final INotificationService notificationService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Registration register(Long activityId, Long userId) {
        Activity activity = activityService.getById(activityId);
        if (activity == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "活动不存在");
        }
        if (!ActivityStatusEnum.RECRUITING.name().equals(activity.getStatus())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "活动不在招募中，无法报名");
        }
        if (activity.getAppliedPeople() >= activity.getNeedPeople()) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "活动报名人数已满");
        }

        Registration existing = lambdaQuery()
                .eq(Registration::getActivityId, activityId)
                .eq(Registration::getUserId, userId)
                .one();
        if (existing != null && !RegistrationStatusEnum.CANCELLED.name().equals(existing.getStatus())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "您已报名此活动");
        }

        Registration registration = new Registration();
        registration.setActivityId(activityId);
        registration.setUserId(userId);
        registration.setStatus(RegistrationStatusEnum.PENDING.name());
        
        if (activity.getStartTime().isAfter(LocalDateTime.now().plusHours(24))) {
            registration.setAuditType(AuditTypeEnum.AUTO.name());
            registration.setStatus(RegistrationStatusEnum.APPROVED.name());
            registration.setAuditTime(LocalDateTime.now());
            
            activity.setAppliedPeople(activity.getAppliedPeople() + 1);
            activityService.updateById(activity);
            
            notificationService.create(userId, NotificationTypeEnum.ACTIVITY.name(),
                    "报名成功通知", "您已成功报名活动：" + activity.getTitle(), activityId);
        } else {
            registration.setAuditType(AuditTypeEnum.MANUAL.name());
            notificationService.create(userId, NotificationTypeEnum.ACTIVITY.name(),
                    "报名提交成功", "您的活动报名已提交，等待人工审核", activityId);
        }

        save(registration);
        return registration;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void audit(RegistrationAuditDTO dto, Long auditorId) {
        Registration registration = getById(dto.getRegistrationId());
        if (registration == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "报名记录不存在");
        }
        if (!RegistrationStatusEnum.PENDING.name().equals(registration.getStatus())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "报名记录状态不正确");
        }

        registration.setStatus(dto.getStatus());
        registration.setAuditId(auditorId);
        registration.setAuditTime(LocalDateTime.now());
        registration.setAuditReason(dto.getAuditReason());
        registration.setAuditType(AuditTypeEnum.MANUAL.name());

        updateById(registration);

        if (RegistrationStatusEnum.APPROVED.name().equals(dto.getStatus())) {
            Activity activity = activityService.getById(registration.getActivityId());
            activity.setAppliedPeople(activity.getAppliedPeople() + 1);
            activityService.updateById(activity);
            
            notificationService.create(registration.getUserId(), NotificationTypeEnum.ACTIVITY.name(),
                    "报名审核通过", "您的活动报名已审核通过", registration.getActivityId());
        } else {
            notificationService.create(registration.getUserId(), NotificationTypeEnum.ACTIVITY.name(),
                    "报名审核未通过", "您的活动报名未通过，原因：" + dto.getAuditReason(), registration.getActivityId());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancel(Long registrationId, Long userId) {
        Registration registration = getById(registrationId);
        if (registration == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "报名记录不存在");
        }
        if (!registration.getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权取消他人的报名");
        }
        if (RegistrationStatusEnum.CANCELLED.name().equals(registration.getStatus())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "报名已取消");
        }

        Activity activity = activityService.getById(registration.getActivityId());
        if (ActivityStatusEnum.ONGOING.name().equals(activity.getStatus())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "活动已开始，无法取消报名");
        }

        registration.setStatus(RegistrationStatusEnum.CANCELLED.name());
        updateById(registration);

        if (RegistrationStatusEnum.APPROVED.name().equals(registration.getStatus())) {
            activity.setAppliedPeople(activity.getAppliedPeople() - 1);
            activityService.updateById(activity);
        }
    }

    @Override
    public List<Registration> getMyRegistrations(Long userId) {
        return lambdaQuery()
                .eq(Registration::getUserId, userId)
                .orderByDesc(Registration::getCreateTime)
                .list();
    }

    @Override
    public List<Registration> getActivityRegistrations(Long activityId) {
        return lambdaQuery()
                .eq(Registration::getActivityId, activityId)
                .eq(Registration::getStatus, RegistrationStatusEnum.APPROVED.name())
                .orderByDesc(Registration::getCreateTime)
                .list();
    }
}
