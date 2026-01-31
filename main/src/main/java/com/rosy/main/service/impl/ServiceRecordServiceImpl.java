package com.rosy.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.entity.Activity;
import com.rosy.main.domain.entity.Enrollment;
import com.rosy.main.domain.entity.ServiceRecord;
import com.rosy.main.domain.entity.User;
import com.rosy.main.enums.ActivityStatusEnum;
import com.rosy.main.enums.EnrollmentStatusEnum;
import com.rosy.main.mapper.ServiceRecordMapper;
import com.rosy.main.service.IActivityService;
import com.rosy.main.service.IEnrollmentService;
import com.rosy.main.service.IServiceRecordService;
import com.rosy.main.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 服务记录Service实现类
 */
@Service
public class ServiceRecordServiceImpl extends ServiceImpl<ServiceRecordMapper, ServiceRecord> implements IServiceRecordService {

    @Autowired
    private IActivityService activityService;
    
    @Autowired
    private IEnrollmentService enrollmentService;
    
    @Autowired
    private IUserService userService;

    @Override
    public Page<ServiceRecord> getServiceRecordPage(Page<ServiceRecord> page, Long activityId, Long userId, Integer rating) {
        QueryWrapper<ServiceRecord> queryWrapper = new QueryWrapper<>();
        
        // 添加查询条件
        if (activityId != null) {
            queryWrapper.eq("activity_id", activityId);
        }
        if (userId != null) {
            queryWrapper.eq("user_id", userId);
        }
        if (rating != null) {
            queryWrapper.eq("rating", rating);
        }
        
        // 按创建时间倒序排列
        queryWrapper.orderByDesc("create_time");
        
        return this.page(page, queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean checkIn(Long activityId, Long userId) {
        // 检查活动是否存在
        Activity activity = activityService.getById(activityId);
        if (activity == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "活动不存在");
        }
        
        // 检查活动状态是否为进行中
        if (!ActivityStatusEnum.IN_PROGRESS.getCode().equals(activity.getStatus())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "活动未开始或已结束");
        }
        
        // 检查用户是否已报名并通过审核
        QueryWrapper<Enrollment> enrollmentQuery = new QueryWrapper<>();
        enrollmentQuery.eq("activity_id", activityId);
        enrollmentQuery.eq("user_id", userId);
        enrollmentQuery.eq("status", EnrollmentStatusEnum.APPROVED.getCode());
        Enrollment enrollment = enrollmentService.getOne(enrollmentQuery);
        if (enrollment == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "用户未报名或报名未通过审核");
        }
        
        // 检查是否已签到
        if (isCheckedIn(activityId, userId)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "用户已签到");
        }
        
        // 创建或更新服务记录
        ServiceRecord serviceRecord = new ServiceRecord();
        serviceRecord.setActivityId(activityId);
        serviceRecord.setUserId(userId);
        serviceRecord.setCheckInTime(LocalDateTime.now());
        
        return this.saveOrUpdate(serviceRecord, new QueryWrapper<ServiceRecord>()
                .eq("activity_id", activityId)
                .eq("user_id", userId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean checkOut(Long activityId, Long userId) {
        // 检查是否已签到
        if (!isCheckedIn(activityId, userId)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "用户未签到");
        }
        
        // 检查是否已签出
        if (isCheckedOut(activityId, userId)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "用户已签出");
        }
        
        // 获取服务记录
        QueryWrapper<ServiceRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("activity_id", activityId);
        queryWrapper.eq("user_id", userId);
        ServiceRecord serviceRecord = this.getOne(queryWrapper);
        
        if (serviceRecord == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "服务记录不存在");
        }
        
        // 设置签出时间
        serviceRecord.setCheckOutTime(LocalDateTime.now());
        
        // 计算服务时长（小时）
        if (serviceRecord.getCheckInTime() != null) {
            Duration duration = Duration.between(serviceRecord.getCheckInTime(), serviceRecord.getCheckOutTime());
            // 转换为小时，保留两位小数
            BigDecimal hours = BigDecimal.valueOf(duration.toMinutes())
                    .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);
            serviceRecord.setServiceHours(hours);
        }
        
        return this.updateById(serviceRecord);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean rateService(Long activityId, Long userId, Integer rating, String feedback) {
        // 验证评价星级
        if (rating < 1 || rating > 5) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "评价星级必须在1-5之间");
        }
        
        // 检查是否已签出
        if (!isCheckedOut(activityId, userId)) {
            throw new BusinessException("请先签出后再评价");
        }
        
        // 获取服务记录
        QueryWrapper<ServiceRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("activity_id", activityId);
        queryWrapper.eq("user_id", userId);
        ServiceRecord serviceRecord = this.getOne(queryWrapper);
        
        if (serviceRecord == null) {
            throw new BusinessException("服务记录不存在");
        }
        
        // 更新评价
        serviceRecord.setRating(rating);
        serviceRecord.setFeedback(feedback);
        
        return this.updateById(serviceRecord);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String generateCertificate(Long activityId, Long userId) {
        // 检查是否已签出
        if (!isCheckedOut(activityId, userId)) {
            throw new BusinessException("请先完成签到签出");
        }
        
        // 获取服务记录
        QueryWrapper<ServiceRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("activity_id", activityId);
        queryWrapper.eq("user_id", userId);
        ServiceRecord serviceRecord = this.getOne(queryWrapper);
        
        if (serviceRecord == null) {
            throw new BusinessException("服务记录不存在");
        }
        
        // 检查是否已生成证明
        if (serviceRecord.getCertificateId() != null) {
            return serviceRecord.getCertificateId();
        }
        
        // 生成证明编号
        String certificateId = "VOL" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        
        // 更新服务记录
        serviceRecord.setCertificateId(certificateId);
        this.updateById(serviceRecord);
        
        // 更新用户总服务时长
        updateTotalServiceHours(userId);
        
        return certificateId;
    }

    @Override
    public BigDecimal calculateTotalServiceHours(Long userId) {
        QueryWrapper<ServiceRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.isNotNull("service_hours");
        
        // 查询用户所有服务记录
        java.util.List<ServiceRecord> records = this.list(queryWrapper);
        
        // 计算总时长
        BigDecimal totalHours = BigDecimal.ZERO;
        for (ServiceRecord record : records) {
            if (record.getServiceHours() != null) {
                totalHours = totalHours.add(record.getServiceHours());
            }
        }
        
        return totalHours;
    }

    @Override
    public boolean isCheckedIn(Long activityId, Long userId) {
        QueryWrapper<ServiceRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("activity_id", activityId);
        queryWrapper.eq("user_id", userId);
        queryWrapper.isNotNull("check_in_time");
        
        return this.count(queryWrapper) > 0;
    }

    @Override
    public boolean isCheckedOut(Long activityId, Long userId) {
        QueryWrapper<ServiceRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("activity_id", activityId);
        queryWrapper.eq("user_id", userId);
        queryWrapper.isNotNull("check_out_time");
        
        return this.count(queryWrapper) > 0;
    }
    
    /**
     * 更新用户总服务时长
     *
     * @param userId 用户ID
     */
    private void updateTotalServiceHours(Long userId) {
        BigDecimal totalHours = calculateTotalServiceHours(userId);
        
        User user = userService.getById(userId);
        if (user != null) {
            user.setTotalHours(totalHours);
            userService.updateById(user);
        }
    }
}