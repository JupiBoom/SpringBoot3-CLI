package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.enums.RegistrationStatusEnum;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.QueryWrapperUtil;
import com.rosy.main.domain.dto.registration.RegistrationQueryRequest;
import com.rosy.main.domain.entity.Activity;
import com.rosy.main.domain.entity.Registration;
import com.rosy.main.domain.entity.ServiceRecord;
import com.rosy.main.domain.entity.User;
import com.rosy.main.domain.vo.RegistrationVO;
import com.rosy.main.mapper.ActivityMapper;
import com.rosy.main.mapper.RegistrationMapper;
import com.rosy.main.mapper.ServiceRecordMapper;
import com.rosy.main.mapper.UserMapper;
import com.rosy.main.service.INotificationService;
import com.rosy.main.service.IRegistrationService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class RegistrationServiceImpl extends ServiceImpl<RegistrationMapper, Registration> implements IRegistrationService {

    @Resource
    private ActivityMapper activityMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private ServiceRecordMapper serviceRecordMapper;

    @Resource
    private INotificationService notificationService;

    @Override
    public RegistrationVO getRegistrationVO(Registration registration) {
        if (registration == null) {
            return null;
        }
        RegistrationVO vo = BeanUtil.copyProperties(registration, RegistrationVO.class);

        RegistrationStatusEnum statusEnum = RegistrationStatusEnum.getByCode(registration.getStatus());
        if (statusEnum != null) {
            vo.setStatusDesc(statusEnum.getDesc());
        }

        if (registration.getActivityId() != null) {
            Activity activity = activityMapper.selectById(registration.getActivityId());
            if (activity != null) {
                vo.setActivityTitle(activity.getTitle());
            }
        }

        if (registration.getUserId() != null) {
            User user = userMapper.selectById(registration.getUserId());
            if (user != null) {
                vo.setUserName(user.getRealName() != null ? user.getRealName() : user.getUsername());
                vo.setUserPhone(user.getPhone());
            }
        }

        if (registration.getAuditUserId() != null) {
            User auditUser = userMapper.selectById(registration.getAuditUserId());
            if (auditUser != null) {
                vo.setAuditUserName(auditUser.getRealName() != null ? auditUser.getRealName() : auditUser.getUsername());
            }
        }

        return vo;
    }

    @Override
    public LambdaQueryWrapper<Registration> getQueryWrapper(RegistrationQueryRequest queryRequest) {
        if (queryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        LambdaQueryWrapper<Registration> queryWrapper = new LambdaQueryWrapper<>();

        QueryWrapperUtil.addCondition(queryWrapper, queryRequest.getId(), Registration::getId);
        QueryWrapperUtil.addCondition(queryWrapper, queryRequest.getActivityId(), Registration::getActivityId);
        QueryWrapperUtil.addCondition(queryWrapper, queryRequest.getUserId(), Registration::getUserId);
        QueryWrapperUtil.addCondition(queryWrapper, queryRequest.getStatus(), Registration::getStatus);

        QueryWrapperUtil.addSortCondition(queryWrapper,
                queryRequest.getSortField(),
                queryRequest.getSortOrder(),
                Registration::getCreateTime);

        return queryWrapper;
    }

    @Override
    @Transactional
    public boolean register(Long activityId, Long userId) {
        Activity activity = activityMapper.selectById(activityId);
        if (activity == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "活动不存在");
        }

        if (activity.getCurrentCount() >= activity.getRequiredCount()) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "活动报名人数已满");
        }

        LambdaQueryWrapper<Registration> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Registration::getActivityId, activityId)
                .eq(Registration::getUserId, userId);
        Registration existing = this.getOne(queryWrapper);
        if (existing != null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "您已报名该活动");
        }

        Registration registration = new Registration();
        registration.setActivityId(activityId);
        registration.setUserId(userId);

        if (activity.getAutoApprove() != null && activity.getAutoApprove() == (byte) 1) {
            registration.setStatus((byte) RegistrationStatusEnum.APPROVED.getCode());
            activityMapper.incrementCurrentCount(activityId);
        } else {
            registration.setStatus((byte) RegistrationStatusEnum.PENDING.getCode());
        }

        boolean result = this.save(registration);
        if (result && activity.getAutoApprove() != null && activity.getAutoApprove() == (byte) 1) {
            notificationService.sendRegistrationAuditNotice(registration.getId(), true);
        }

        return result;
    }

    @Override
    @Transactional
    public boolean audit(Long registrationId, Byte status, String auditRemark, Long auditUserId) {
        Registration registration = this.getById(registrationId);
        if (registration == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "报名记录不存在");
        }

        if (registration.getStatus() != RegistrationStatusEnum.PENDING.getCode()) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "该报名已审核");
        }

        registration.setStatus(status);
        registration.setAuditUserId(auditUserId);
        registration.setAuditTime(LocalDateTime.now());
        registration.setAuditRemark(auditRemark);

        boolean result = this.updateById(registration);

        if (result && status == RegistrationStatusEnum.APPROVED.getCode()) {
            activityMapper.incrementCurrentCount(registration.getActivityId());
        }

        if (result) {
            notificationService.sendRegistrationAuditNotice(registrationId, status == RegistrationStatusEnum.APPROVED.getCode());
        }

        return result;
    }

    @Override
    @Transactional
    public boolean checkIn(Long activityId, Long userId, String checkInLocation) {
        LambdaQueryWrapper<Registration> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Registration::getActivityId, activityId)
                .eq(Registration::getUserId, userId)
                .eq(Registration::getStatus, RegistrationStatusEnum.APPROVED.getCode());
        Registration registration = this.getOne(queryWrapper);

        if (registration == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "未找到有效的报名记录");
        }

        if (registration.getCheckInTime() != null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "已签到，请勿重复签到");
        }

        registration.setCheckInTime(LocalDateTime.now());
        registration.setCheckInLocation(checkInLocation);

        return this.updateById(registration);
    }

    @Override
    @Transactional
    public boolean checkOut(Long activityId, Long userId, String checkOutLocation) {
        LambdaQueryWrapper<Registration> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Registration::getActivityId, activityId)
                .eq(Registration::getUserId, userId)
                .eq(Registration::getStatus, RegistrationStatusEnum.APPROVED.getCode());
        Registration registration = this.getOne(queryWrapper);

        if (registration == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "未找到有效的报名记录");
        }

        if (registration.getCheckInTime() == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "请先签到");
        }

        if (registration.getCheckOutTime() != null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "已签退，请勿重复签退");
        }

        LocalDateTime now = LocalDateTime.now();
        registration.setCheckOutTime(now);
        registration.setCheckOutLocation(checkOutLocation);

        boolean result = this.updateById(registration);

        if (result) {
            createServiceRecord(registration, now);
        }

        return result;
    }

    private void createServiceRecord(Registration registration, LocalDateTime checkOutTime) {
        Activity activity = activityMapper.selectById(registration.getActivityId());
        if (activity == null) {
            return;
        }

        ServiceRecord serviceRecord = new ServiceRecord();
        serviceRecord.setActivityId(registration.getActivityId());
        serviceRecord.setUserId(registration.getUserId());
        serviceRecord.setRegistrationId(registration.getId());
        serviceRecord.setServiceDate(LocalDate.now());
        serviceRecord.setStartTime(registration.getCheckInTime());
        serviceRecord.setEndTime(checkOutTime);

        Duration duration = Duration.between(registration.getCheckInTime(), checkOutTime);
        BigDecimal hours = BigDecimal.valueOf(duration.toMinutes())
                .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);
        serviceRecord.setDurationHours(hours);

        serviceRecordMapper.insert(serviceRecord);
    }
}
