package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.dto.activity.ActivityRegistrationAddRequest;
import com.rosy.main.domain.dto.activity.ActivityRegistrationAuditRequest;
import com.rosy.main.domain.dto.activity.ActivityRegistrationQueryRequest;
import com.rosy.main.domain.entity.Activity;
import com.rosy.main.domain.entity.ActivityRegistration;
import com.rosy.main.domain.vo.ActivityRegistrationVO;
import com.rosy.main.mapper.ActivityRegistrationMapper;
import com.rosy.main.service.IActivityRegistrationService;
import com.rosy.main.service.IActivityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 活动报名服务实现类
 *
 * @author Rosy
 * @since 2026-01-17
 */
@Service
public class ActivityRegistrationServiceImpl extends ServiceImpl<ActivityRegistrationMapper, ActivityRegistration> implements IActivityRegistrationService {

    @Resource
    private IActivityService activityService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long registerActivity(ActivityRegistrationAddRequest request) {
        // 1. 检查活动是否存在
        Activity activity = activityService.getById(request.getActivityId());
        if (activity == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "活动不存在");
        }

        // 2. 检查活动是否已结束
        if (activity.getActivityTime().isBefore(LocalDateTime.now())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "活动已结束，无法报名");
        }

        // 3. 检查活动是否已满
        if (activity.getRegisteredPeople() != null && activity.getRequiredPeople() != null
                && activity.getRegisteredPeople() >= activity.getRequiredPeople()) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "活动报名人数已满");
        }

        // 4. 检查是否已报名
        LambdaQueryWrapper<ActivityRegistration> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ActivityRegistration::getActivityId, request.getActivityId())
                .eq(ActivityRegistration::getVolunteerId, request.getVolunteerId())
                .eq(ActivityRegistration::getIsDeleted, 0);
        if (this.count(wrapper) > 0) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "您已报名此活动");
        }

        // 5. 创建报名记录
        ActivityRegistration registration = BeanUtil.copyProperties(request, ActivityRegistration.class);
        registration.setAuditStatus((byte) 0); // 待审核
        registration.setIsSignedIn((byte) 0); // 未签到
        registration.setIsSignedOut((byte) 0); // 未签出
        this.save(registration);

        // 6. 更新活动报名人数
        activityService.update()
                .setSql("registered_people = registered_people + 1")
                .eq("id", request.getActivityId())
                .update();

        return registration.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean auditRegistration(Long id, ActivityRegistrationAuditRequest request) {
        ActivityRegistration registration = this.getById(id);
        if (registration == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "报名记录不存在");
        }

        if (registration.getAuditStatus() != null && registration.getAuditStatus() != 0) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "该报名已审核过");
        }

        return this.update()
                .set("audit_status", request.getAuditStatus())
                .set("audit_remark", request.getAuditRemark())
                .set("audit_time", LocalDateTime.now())
                .eq("id", id)
                .update();
    }

    @Override
    public boolean signIn(Long registrationId) {
        ActivityRegistration registration = this.getById(registrationId);
        if (registration == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "报名记录不存在");
        }

        if (registration.getAuditStatus() == null || registration.getAuditStatus() != 1) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "报名未通过审核，无法签到");
        }

        if (registration.getIsSignedIn() != null && registration.getIsSignedIn() == 1) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "已签到，请勿重复签到");
        }

        return this.update()
                .set("is_signed_in", 1)
                .set("sign_in_time", LocalDateTime.now())
                .eq("id", registrationId)
                .update();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean signOut(Long registrationId) {
        ActivityRegistration registration = this.getById(registrationId);
        if (registration == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "报名记录不存在");
        }

        if (registration.getIsSignedIn() == null || registration.getIsSignedIn() == 0) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "未签到，无法签出");
        }

        if (registration.getIsSignedOut() != null && registration.getIsSignedOut() == 1) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "已签出，请勿重复签出");
        }

        return this.update()
                .set("is_signed_out", 1)
                .set("sign_out_time", LocalDateTime.now())
                .eq("id", registrationId)
                .update();
    }

    @Override
    public List<ActivityRegistration> getRemindRegistrations(LocalDateTime activityTime) {
        LocalDateTime remindTime = activityTime.minusHours(1);
        LocalDateTime now = LocalDateTime.now();

        LambdaQueryWrapper<ActivityRegistration> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ActivityRegistration::getAuditStatus, 1)
                .eq(ActivityRegistration::getIsSignedIn, 0)
                .between(ActivityRegistration::getCreateTime, now, remindTime);

        return this.list(wrapper);
    }

    @Override
    public ActivityRegistrationVO getActivityRegistrationVO(ActivityRegistration registration) {
        ActivityRegistrationVO vo = BeanUtil.copyProperties(registration, ActivityRegistrationVO.class);

        // 设置签到签出状态
        if (registration.getIsSignedIn() != null) {
            vo.setCheckInStatus(registration.getIsSignedIn() == 1 ? 1 : 0);
        }
        if (registration.getIsSignedOut() != null) {
            vo.setCheckOutStatus(registration.getIsSignedOut() == 1 ? 1 : 0);
        }

        // 设置签到签出时间
        vo.setCheckInTime(registration.getSignInTime());
        vo.setCheckOutTime(registration.getSignOutTime());

        // 计算服务时长（分钟）
        if (registration.getSignInTime() != null && registration.getSignOutTime() != null) {
            Duration duration = Duration.between(registration.getSignInTime(), registration.getSignOutTime());
            long minutes = duration.toMinutes();
            vo.setServiceDuration(minutes > 0 ? (int) minutes : 0);
        }

        // 计算服务时长
        if (registration.getSignInTime() != null && registration.getSignOutTime() != null) {
            Duration duration = Duration.between(registration.getSignInTime(), registration.getSignOutTime());
            vo.setServiceDuration((int) duration.toMinutes());
        }

        return vo;
    }

    @Override
    public List<ActivityRegistrationVO> getVolunteerRegistrations(Long volunteerId, ActivityRegistrationQueryRequest request) {
        LambdaQueryWrapper<ActivityRegistration> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ActivityRegistration::getVolunteerId, volunteerId)
                .eq(ActivityRegistration::getIsDeleted, 0);

        if (request.getActivityId() != null) {
            wrapper.eq(ActivityRegistration::getActivityId, request.getActivityId());
        }
        if (request.getAuditStatus() != null) {
            wrapper.eq(ActivityRegistration::getAuditStatus, request.getAuditStatus());
        }
        wrapper.orderByDesc(ActivityRegistration::getCreateTime);

        return this.list(wrapper).stream()
                .map(this::getActivityRegistrationVO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ActivityRegistrationVO> getActivityRegistrations(Long activityId, ActivityRegistrationQueryRequest request) {
        LambdaQueryWrapper<ActivityRegistration> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ActivityRegistration::getActivityId, activityId)
                .eq(ActivityRegistration::getIsDeleted, 0);

        if (request.getAuditStatus() != null) {
            wrapper.eq(ActivityRegistration::getAuditStatus, request.getAuditStatus());
        }
        wrapper.orderByDesc(ActivityRegistration::getCreateTime);

        return this.list(wrapper).stream()
                .map(this::getActivityRegistrationVO)
                .collect(Collectors.toList());
    }
}