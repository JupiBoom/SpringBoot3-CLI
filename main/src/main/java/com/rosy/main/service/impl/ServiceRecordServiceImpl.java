package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.entity.Activity;
import com.rosy.main.domain.entity.Registration;
import com.rosy.main.domain.entity.ServiceRecord;
import com.rosy.main.domain.entity.User;
import com.rosy.main.domain.vo.ActivityVO;
import com.rosy.main.domain.vo.ServiceRecordVO;
import com.rosy.main.domain.vo.UserVO;
import com.rosy.main.mapper.ServiceRecordMapper;
import com.rosy.main.service.IActivityService;
import com.rosy.main.service.IRegistrationService;
import com.rosy.main.service.IServiceRecordService;
import com.rosy.main.service.IUserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class ServiceRecordServiceImpl extends ServiceImpl<ServiceRecordMapper, ServiceRecord> implements IServiceRecordService {

    @Resource
    private IActivityService activityService;

    @Resource
    private IRegistrationService registrationService;

    @Resource
    private IUserService userService;

    @Override
    public ServiceRecordVO getServiceRecordVO(ServiceRecord serviceRecord) {
        if (serviceRecord == null) {
            return null;
        }
        ServiceRecordVO serviceRecordVO = BeanUtil.copyProperties(serviceRecord, ServiceRecordVO.class);

        Activity activity = activityService.getById(serviceRecord.getActivityId());
        if (activity != null) {
            serviceRecordVO.setActivityTitle(activity.getTitle());
        }

        User user = userService.getById(serviceRecord.getUserId());
        if (user != null) {
            serviceRecordVO.setUserName(user.getRealName());
        }

        return serviceRecordVO;
    }

    @Override
    public LambdaQueryWrapper<ServiceRecord> getQueryWrapper(Long activityId, Long userId) {
        LambdaQueryWrapper<ServiceRecord> queryWrapper = new LambdaQueryWrapper<>();
        if (activityId != null) {
            queryWrapper.eq(ServiceRecord::getActivityId, activityId);
        }
        if (userId != null) {
            queryWrapper.eq(ServiceRecord::getUserId, userId);
        }
        queryWrapper.orderByDesc(ServiceRecord::getCheckInTime);
        return queryWrapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean checkIn(Long activityId, Long userId) {
        Activity activity = activityService.getById(activityId);
        if (activity == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "活动不存在");
        }

        LambdaQueryWrapper<Registration> regWrapper = new LambdaQueryWrapper<>();
        regWrapper.eq(Registration::getActivityId, activityId)
                .eq(Registration::getUserId, userId)
                .eq(Registration::getStatus, (byte) 1);
        Registration registration = registrationService.getOne(regWrapper);
        if (registration == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "未报名或报名未通过");
        }

        LambdaQueryWrapper<ServiceRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ServiceRecord::getActivityId, activityId)
                .eq(ServiceRecord::getUserId, userId);
        ServiceRecord existingRecord = getOne(wrapper);

        if (existingRecord != null && existingRecord.getCheckInTime() != null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "已签到");
        }

        ServiceRecord serviceRecord = existingRecord != null ? existingRecord : new ServiceRecord();
        serviceRecord.setActivityId(activityId);
        serviceRecord.setUserId(userId);
        serviceRecord.setCheckInTime(LocalDateTime.now());

        if (existingRecord == null) {
            return save(serviceRecord);
        }
        return updateById(serviceRecord);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean checkOut(Long activityId, Long userId) {
        LambdaQueryWrapper<ServiceRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ServiceRecord::getActivityId, activityId)
                .eq(ServiceRecord::getUserId, userId);
        ServiceRecord serviceRecord = getOne(wrapper);

        if (serviceRecord == null || serviceRecord.getCheckInTime() == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "未签到");
        }
        if (serviceRecord.getCheckOutTime() != null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "已签出");
        }

        serviceRecord.setCheckOutTime(LocalDateTime.now());

        long minutes = ChronoUnit.MINUTES.between(serviceRecord.getCheckInTime(), serviceRecord.getCheckOutTime());
        BigDecimal hours = BigDecimal.valueOf(minutes).divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);
        serviceRecord.setServiceHours(hours);

        return updateById(serviceRecord);
    }

    @Override
    public boolean rateService(Long activityId, Long userId, Byte rating, String comment) {
        LambdaQueryWrapper<ServiceRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ServiceRecord::getActivityId, activityId)
                .eq(ServiceRecord::getUserId, userId);
        ServiceRecord serviceRecord = getOne(wrapper);

        if (serviceRecord == null || serviceRecord.getCheckOutTime() == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "服务未完成");
        }

        serviceRecord.setRating(rating);
        serviceRecord.setComment(comment);

        return updateById(serviceRecord);
    }

    @Override
    public String generateCertificate(Long activityId, Long userId) {
        LambdaQueryWrapper<ServiceRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ServiceRecord::getActivityId, activityId)
                .eq(ServiceRecord::getUserId, userId);
        ServiceRecord serviceRecord = getOne(wrapper);

        if (serviceRecord == null || serviceRecord.getCheckOutTime() == null || serviceRecord.getRating() == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "服务未完成或未评价");
        }

        String certificateUrl = "/certificates/" + activityId + "_" + userId + ".pdf";
        serviceRecord.setCertificateUrl(certificateUrl);
        updateById(serviceRecord);

        return certificateUrl;
    }
}
