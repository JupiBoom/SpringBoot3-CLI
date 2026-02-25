package com.rosy.main.service.impl;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.enums.RegistrationStatusEnum;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.dto.ServiceRatingDTO;
import com.rosy.main.domain.entity.Activity;
import com.rosy.main.domain.entity.Registration;
import com.rosy.main.domain.entity.ServiceRecord;
import com.rosy.main.domain.entity.User;
import com.rosy.main.mapper.ServiceRecordMapper;
import com.rosy.main.service.IActivityService;
import com.rosy.main.service.IRegistrationService;
import com.rosy.main.service.IServiceRecordService;
import com.rosy.main.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceRecordServiceImpl extends ServiceImpl<ServiceRecordMapper, ServiceRecord> implements IServiceRecordService {

    private final IRegistrationService registrationService;
    private final IActivityService activityService;
    private final IUserService userService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServiceRecord checkIn(Long registrationId, Long userId) {
        Registration registration = registrationService.getById(registrationId);
        if (registration == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "报名记录不存在");
        }
        if (!registration.getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权操作");
        }
        if (!RegistrationStatusEnum.APPROVED.name().equals(registration.getStatus())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "报名未通过审核，无法签到");
        }

        Activity activity = activityService.getById(registration.getActivityId());
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(activity.getStartTime().minusHours(1))) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "活动开始前1小时才能签到");
        }
        if (now.isAfter(activity.getEndTime())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "活动已结束，无法签到");
        }

        ServiceRecord existing = lambdaQuery()
                .eq(ServiceRecord::getRegistrationId, registrationId)
                .one();
        if (existing != null && existing.getCheckInTime() != null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "您已签到");
        }

        ServiceRecord record;
        if (existing == null) {
            record = new ServiceRecord();
            record.setActivityId(registration.getActivityId());
            record.setRegistrationId(registrationId);
            record.setUserId(userId);
        } else {
            record = existing;
        }
        record.setCheckInTime(now);
        
        saveOrUpdate(record);
        return record;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServiceRecord checkOut(Long registrationId, Long userId) {
        ServiceRecord record = lambdaQuery()
                .eq(ServiceRecord::getRegistrationId, registrationId)
                .eq(ServiceRecord::getUserId, userId)
                .one();
        if (record == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "未找到签到记录");
        }
        if (record.getCheckInTime() == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "请先签到");
        }
        if (record.getCheckOutTime() != null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "您已签出");
        }

        LocalDateTime now = LocalDateTime.now();
        record.setCheckOutTime(now);
        
        long minutes = DateUtil.between(DateUtil.date(record.getCheckInTime()), DateUtil.date(now), DateUnit.MINUTE);
        BigDecimal hours = BigDecimal.valueOf(minutes).divide(BigDecimal.valueOf(60), 2, java.math.RoundingMode.HALF_UP);
        record.setServiceHours(hours);

        updateById(record);

        User user = userService.getById(userId);
        user.setTotalHours(user.getTotalHours().add(hours));
        userService.updateById(user);

        return record;
    }

    @Override
    public void rate(ServiceRatingDTO dto, Long userId) {
        ServiceRecord record = getById(dto.getServiceRecordId());
        if (record == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "服务记录不存在");
        }
        if (!record.getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权评价");
        }
        if (record.getCheckOutTime() == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "活动结束后才能评价");
        }
        if (record.getRating() != null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "您已评价过此活动");
        }
        if (dto.getRating() < 1 || dto.getRating() > 5) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "评分必须在1-5之间");
        }

        record.setRating(dto.getRating());
        record.setComment(dto.getComment());
        updateById(record);
    }

    @Override
    public String generateCertificate(Long serviceRecordId, Long userId) {
        ServiceRecord record = getById(serviceRecordId);
        if (record == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "服务记录不存在");
        }
        if (!record.getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权生成证书");
        }
        if (record.getCheckOutTime() == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "活动未完成，无法生成证书");
        }
        if (record.getCertificateGenerated() == 1) {
            return record.getCertificateUrl();
        }

        Activity activity = activityService.getById(record.getActivityId());
        User user = userService.getById(userId);
        
        String certificateUrl = "/certificates/" + serviceRecordId + ".pdf";
        record.setCertificateGenerated(1);
        record.setCertificateUrl(certificateUrl);
        updateById(record);

        return certificateUrl;
    }

    @Override
    public List<ServiceRecord> getMyServiceRecords(Long userId) {
        return lambdaQuery()
                .eq(ServiceRecord::getUserId, userId)
                .orderByDesc(ServiceRecord::getCreateTime)
                .list();
    }
}
