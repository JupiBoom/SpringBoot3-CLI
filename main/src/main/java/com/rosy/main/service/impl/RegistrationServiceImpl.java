package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ActivityStatusEnum;
import com.rosy.common.enums.RegistrationStatusEnum;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.dto.RegistrationDTO;
import com.rosy.main.domain.dto.RegistrationReviewDTO;
import com.rosy.main.domain.entity.Activity;
import com.rosy.main.domain.entity.Registration;
import com.rosy.main.domain.entity.ServiceRecord;
import com.rosy.main.domain.vo.RegistrationVO;
import com.rosy.main.mapper.ActivityMapper;
import com.rosy.main.mapper.RegistrationMapper;
import com.rosy.main.mapper.ServiceRecordMapper;
import com.rosy.main.service.IRegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl extends ServiceImpl<RegistrationMapper, Registration> implements IRegistrationService {

    private final ActivityMapper activityMapper;
    private final ServiceRecordMapper serviceRecordMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createRegistration(RegistrationDTO dto, Long userId) {
        Activity activity = activityMapper.selectById(dto.getActivityId());
        if (activity == null) {
            throw new BusinessException("活动不存在");
        }

        if (!ActivityStatusEnum.RECRUITING.getValue().equals(activity.getStatus())) {
            throw new BusinessException("活动不在招募中");
        }

        if (activity.getEndTime().isBefore(LocalDateTime.now())) {
            throw new BusinessException("活动已结束");
        }

        Registration existing = lambdaQuery()
                .eq(Registration::getActivityId, dto.getActivityId())
                .eq(Registration::getUserId, userId)
                .one();
        if (existing != null) {
            throw new BusinessException("您已报名此活动");
        }

        if (activity.getRegisteredNum() >= activity.getRequiredNum()) {
            throw new BusinessException("活动报名人数已满");
        }

        Registration registration = BeanUtil.copyProperties(dto, Registration.class);
        registration.setUserId(userId);
        registration.setStatus(RegistrationStatusEnum.PENDING.getValue());
        registration.setApplyTime(LocalDateTime.now());
        save(registration);

        autoReviewRegistration(registration.getId());

        return registration.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void autoReviewRegistration(Long registrationId) {
        Registration registration = getById(registrationId);
        if (registration == null) {
            throw new BusinessException("报名记录不存在");
        }

        if (!RegistrationStatusEnum.PENDING.getValue().equals(registration.getStatus())) {
            return;
        }

        Activity activity = activityMapper.selectById(registration.getActivityId());
        if (activity == null) {
            return;
        }

        boolean shouldApprove = activity.getRegisteredNum() < activity.getRequiredNum()
                && LocalDateTime.now().isBefore(activity.getStartTime().minusHours(1));

        if (shouldApprove) {
            registration.setStatus(RegistrationStatusEnum.APPROVED.getValue());
            registration.setReviewTime(LocalDateTime.now());
            registration.setReviewRemark("系统自动审核通过");
            updateById(registration);

            activity.setRegisteredNum(activity.getRegisteredNum() + 1);
            activityMapper.updateById(activity);

            ServiceRecord serviceRecord = new ServiceRecord();
            serviceRecord.setActivityId(activity.getId());
            serviceRecord.setUserId(registration.getUserId());
            serviceRecord.setRegistrationId(registration.getId());
            serviceRecord.setServiceHours(0);
            serviceRecord.setCreatedTime(LocalDateTime.now());
            serviceRecordMapper.insert(serviceRecord);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void manualReviewRegistration(RegistrationReviewDTO dto, Long reviewerId) {
        Registration registration = getById(dto.getRegistrationId());
        if (registration == null) {
            throw new BusinessException("报名记录不存在");
        }

        if (!RegistrationStatusEnum.PENDING.getValue().equals(registration.getStatus())) {
            throw new BusinessException("报名记录已审核");
        }

        RegistrationStatusEnum status = RegistrationStatusEnum.fromValue(dto.getStatus());
        if (status == null) {
            throw new BusinessException("无效的审核状态");
        }

        registration.setStatus(status.getValue());
        registration.setReviewerId(reviewerId);
        registration.setReviewTime(LocalDateTime.now());
        registration.setReviewRemark(dto.getRemark());
        updateById(registration);

        if (RegistrationStatusEnum.APPROVED.equals(status)) {
            Activity activity = activityMapper.selectById(registration.getActivityId());
            if (activity != null) {
                activity.setRegisteredNum(activity.getRegisteredNum() + 1);
                activityMapper.updateById(activity);

                ServiceRecord serviceRecord = new ServiceRecord();
                serviceRecord.setActivityId(activity.getId());
                serviceRecord.setUserId(registration.getUserId());
                serviceRecord.setRegistrationId(registration.getId());
                serviceRecord.setServiceHours(0);
                serviceRecord.setCreatedTime(LocalDateTime.now());
                serviceRecordMapper.insert(serviceRecord);
            }
        }
    }

    @Override
    public RegistrationVO getRegistrationDetail(Long registrationId) {
        Registration registration = getById(registrationId);
        if (registration == null) {
            throw new BusinessException("报名记录不存在");
        }
        return BeanUtil.copyProperties(registration, RegistrationVO.class);
    }

    @Override
    public Page<RegistrationVO> getRegistrationByActivity(Long activityId, Integer pageNum, Integer pageSize) {
        Page<Registration> page = new Page<>(pageNum, pageSize);
        Page<Registration> result = lambdaQuery()
                .eq(Registration::getActivityId, activityId)
                .orderByDesc(Registration::getApplyTime)
                .page(page);
        return result.convert(r -> BeanUtil.copyProperties(r, RegistrationVO.class));
    }

    @Override
    public Page<RegistrationVO> getRegistrationByUser(Long userId, Integer pageNum, Integer pageSize) {
        Page<Registration> page = new Page<>(pageNum, pageSize);
        Page<Registration> result = lambdaQuery()
                .eq(Registration::getUserId, userId)
                .orderByDesc(Registration::getApplyTime)
                .page(page);
        return result.convert(r -> BeanUtil.copyProperties(r, RegistrationVO.class));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelRegistration(Long registrationId, Long userId) {
        Registration registration = getById(registrationId);
        if (registration == null) {
            throw new BusinessException("报名记录不存在");
        }

        if (!registration.getUserId().equals(userId)) {
            throw new BusinessException("无权取消他人的报名");
        }

        if (RegistrationStatusEnum.CANCELLED.getValue().equals(registration.getStatus())) {
            throw new BusinessException("报名已取消");
        }

        Activity activity = activityMapper.selectById(registration.getActivityId());
        if (activity != null && LocalDateTime.now().isAfter(activity.getStartTime())) {
            throw new BusinessException("活动已开始，无法取消报名");
        }

        if (RegistrationStatusEnum.APPROVED.getValue().equals(registration.getStatus()) && activity != null) {
            activity.setRegisteredNum(Math.max(0, activity.getRegisteredNum() - 1));
            activityMapper.updateById(activity);
        }

        registration.setStatus(RegistrationStatusEnum.CANCELLED.getValue());
        updateById(registration);
    }
}
