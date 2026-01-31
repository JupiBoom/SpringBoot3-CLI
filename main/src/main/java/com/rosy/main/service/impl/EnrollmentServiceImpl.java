package com.rosy.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.entity.Activity;
import com.rosy.main.domain.entity.Enrollment;
import com.rosy.main.enums.ActivityStatusEnum;
import com.rosy.main.enums.EnrollmentStatusEnum;
import com.rosy.main.mapper.EnrollmentMapper;
import com.rosy.main.service.IActivityService;
import com.rosy.main.service.IEnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 报名Service实现类
 */
@Service
public class EnrollmentServiceImpl extends ServiceImpl<EnrollmentMapper, Enrollment> implements IEnrollmentService {

    @Autowired
    private IActivityService activityService;

    @Override
    public Page<Enrollment> getEnrollmentPage(Page<Enrollment> page, Long activityId, Long userId, Integer status) {
        QueryWrapper<Enrollment> queryWrapper = new QueryWrapper<>();
        
        // 添加查询条件
        if (activityId != null) {
            queryWrapper.eq("activity_id", activityId);
        }
        if (userId != null) {
            queryWrapper.eq("user_id", userId);
        }
        if (status != null) {
            queryWrapper.eq("status", status);
        }
        
        // 按申请时间倒序排列
        queryWrapper.orderByDesc("apply_time");
        
        return this.page(page, queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean enrollActivity(Long activityId, Long userId) {
        // 检查活动是否存在
        Activity activity = activityService.getById(activityId);
        if (activity == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "活动不存在");
        }
        
        // 检查活动状态是否为招募中
        if (!ActivityStatusEnum.RECRUITING.getCode().equals(activity.getStatus())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "活动不在招募中");
        }
        
        // 检查是否还有名额
        if (activity.getCurrentCount() >= activity.getRequiredCount()) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "活动报名人数已满");
        }
        
        // 检查用户是否已报名
        if (isUserEnrolled(activityId, userId)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "用户已报名该活动");
        }
        
        // 创建报名记录
        Enrollment enrollment = new Enrollment();
        enrollment.setActivityId(activityId);
        enrollment.setUserId(userId);
        enrollment.setStatus(EnrollmentStatusEnum.PENDING.getCode());
        enrollment.setApplyTime(LocalDateTime.now());
        
        boolean result = this.save(enrollment);
        
        // 如果报名成功，增加活动当前报名人数
        if (result) {
            activityService.increaseCurrentCount(activityId);
        }
        
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean auditEnrollment(Long enrollmentId, Long auditUserId, Integer status, String reason) {
        // 验证状态值是否合法
        EnrollmentStatusEnum statusEnum = EnrollmentStatusEnum.getByCode(status);
        if (statusEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "无效的状态值");
        }
        
        // 获取报名记录
        Enrollment enrollment = this.getById(enrollmentId);
        if (enrollment == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "报名记录不存在");
        }
        
        // 检查是否已审核
        if (!EnrollmentStatusEnum.PENDING.getCode().equals(enrollment.getStatus())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "报名记录已审核");
        }
        
        // 更新报名记录
        enrollment.setStatus(status);
        enrollment.setAuditTime(LocalDateTime.now());
        enrollment.setAuditUserId(auditUserId);
        enrollment.setAuditReason(reason);
        
        boolean result = this.updateById(enrollment);
        
        // 如果审核拒绝，减少活动当前报名人数
        if (result && EnrollmentStatusEnum.REJECTED.getCode().equals(status)) {
            activityService.decreaseCurrentCount(enrollment.getActivityId());
        }
        
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelEnrollment(Long enrollmentId) {
        // 获取报名记录
        Enrollment enrollment = this.getById(enrollmentId);
        if (enrollment == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "报名记录不存在");
        }
        
        // 检查是否已审核通过
        if (!EnrollmentStatusEnum.APPROVED.getCode().equals(enrollment.getStatus())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "只能取消已审核通过的报名");
        }
        
        // 删除报名记录
        boolean result = this.removeById(enrollmentId);
        
        // 如果删除成功，减少活动当前报名人数
        if (result) {
            activityService.decreaseCurrentCount(enrollment.getActivityId());
        }
        
        return result;
    }

    @Override
    public boolean isUserEnrolled(Long activityId, Long userId) {
        QueryWrapper<Enrollment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("activity_id", activityId);
        queryWrapper.eq("user_id", userId);
        queryWrapper.ne("status", EnrollmentStatusEnum.REJECTED.getCode());
        
        return this.count(queryWrapper) > 0;
    }
}