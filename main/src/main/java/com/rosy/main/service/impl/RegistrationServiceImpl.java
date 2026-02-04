package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.dto.CheckInRequest;
import com.rosy.main.domain.dto.RegistrationCreateRequest;
import com.rosy.main.domain.dto.RegistrationQueryRequest;
import com.rosy.main.domain.dto.RegistrationReviewRequest;
import com.rosy.main.domain.entity.Activity;
import com.rosy.main.domain.entity.Registration;
import com.rosy.main.domain.enums.ActivityStatusEnum;
import com.rosy.main.domain.enums.RegistrationStatusEnum;
import com.rosy.main.domain.vo.RegistrationVO;
import com.rosy.main.mapper.ActivityMapper;
import com.rosy.main.mapper.RegistrationMapper;
import com.rosy.main.service.IRegistrationService;
import com.rosy.main.service.IServiceRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 报名服务实现类
 */
@Service
@Slf4j
public class RegistrationServiceImpl extends ServiceImpl<RegistrationMapper, Registration> implements IRegistrationService {

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private IServiceRecordService serviceRecordService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long register(RegistrationCreateRequest request, Long userId) {
        // 检查活动是否存在
        Activity activity = activityMapper.selectById(request.getActivityId());
        if (activity == null) {
            throw new BusinessException("活动不存在");
        }

        // 检查活动状态
        if (activity.getStatus() != ActivityStatusEnum.RECRUITING.getCode()) {
            throw new BusinessException("活动不在招募中，无法报名");
        }

        // 检查报名时间
        LocalDateTime now = LocalDateTime.now();
        if (activity.getRegistrationStart() != null && now.isBefore(activity.getRegistrationStart())) {
            throw new BusinessException("报名尚未开始");
        }
        if (activity.getRegistrationEnd() != null && now.isAfter(activity.getRegistrationEnd())) {
            throw new BusinessException("报名已结束");
        }

        // 检查是否已报名
        if (hasRegistered(request.getActivityId(), userId)) {
            throw new BusinessException("您已经报名过该活动");
        }

        // 检查人数限制
        if (activity.getConfirmedPeople() >= activity.getRequiredPeople()) {
            throw new BusinessException("活动报名人数已满");
        }

        // 创建报名记录
        Registration registration = new Registration();
        registration.setActivityId(request.getActivityId());
        registration.setUserId(userId);
        registration.setStatus(RegistrationStatusEnum.PENDING.getCode());
        registration.setMessage(request.getMessage());
        registration.setReminderSent(0);

        boolean saved = this.save(registration);
        if (!saved) {
            throw new BusinessException("报名失败");
        }

        // 增加活动已报名人数
        activityMapper.incrementRegisteredPeople(request.getActivityId());

        return registration.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelRegistration(Long registrationId, Long userId) {
        Registration registration = this.getById(registrationId);
        if (registration == null) {
            throw new BusinessException("报名记录不存在");
        }

        // 只能取消自己的报名
        if (!registration.getUserId().equals(userId)) {
            throw new BusinessException("无权操作");
        }

        // 已签到或已签出的不能取消
        if (registration.getStatus() == RegistrationStatusEnum.CHECKED_IN.getCode() ||
                registration.getStatus() == RegistrationStatusEnum.CHECKED_OUT.getCode()) {
            throw new BusinessException("已签到或已完成的活动不能取消报名");
        }

        // 已取消的不能重复取消
        if (registration.getStatus() == RegistrationStatusEnum.CANCELLED.getCode()) {
            throw new BusinessException("报名已取消");
        }

        registration.setStatus(RegistrationStatusEnum.CANCELLED.getCode());
        boolean updated = this.updateById(registration);

        if (updated) {
            // 减少活动已报名人数
            activityMapper.decrementRegisteredPeople(registration.getActivityId());

            // 如果是已通过的报名，减少确认人数
            if (registration.getStatus() == RegistrationStatusEnum.APPROVED.getCode()) {
                activityMapper.decrementConfirmedPeople(registration.getActivityId());
            }
        }

        return updated;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean reviewRegistration(RegistrationReviewRequest request, Long reviewerId) {
        Registration registration = this.getById(request.getId());
        if (registration == null) {
            throw new BusinessException("报名记录不存在");
        }

        // 只能审核待审核状态的报名
        if (registration.getStatus() != RegistrationStatusEnum.PENDING.getCode()) {
            throw new BusinessException("该报名已审核");
        }

        // 检查活动状态
        Activity activity = activityMapper.selectById(registration.getActivityId());
        if (activity == null) {
            throw new BusinessException("活动不存在");
        }

        // 如果是通过审核，检查人数限制
        if (request.getStatus() == RegistrationStatusEnum.APPROVED.getCode()) {
            if (activity.getConfirmedPeople() >= activity.getRequiredPeople()) {
                throw new BusinessException("活动报名人数已满");
            }
        }

        registration.setStatus(request.getStatus());
        registration.setReviewerId(reviewerId);
        registration.setReviewTime(LocalDateTime.now());

        if (request.getStatus() == RegistrationStatusEnum.REJECTED.getCode()) {
            registration.setRejectReason(request.getRejectReason());
        }

        boolean updated = this.updateById(registration);

        if (updated) {
            if (request.getStatus() == RegistrationStatusEnum.APPROVED.getCode()) {
                // 增加活动确认人数
                activityMapper.incrementConfirmedPeople(registration.getActivityId());
            } else if (request.getStatus() == RegistrationStatusEnum.REJECTED.getCode()) {
                // 减少已报名人数
                activityMapper.decrementRegisteredPeople(registration.getActivityId());
            }
        }

        return updated;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean checkIn(CheckInRequest request) {
        Registration registration = this.getById(request.getRegistrationId());
        if (registration == null) {
            throw new BusinessException("报名记录不存在");
        }

        // 只能已通过的报名才能签到
        if (registration.getStatus() != RegistrationStatusEnum.APPROVED.getCode()) {
            throw new BusinessException("报名未通过审核，无法签到");
        }

        // 检查活动状态
        Activity activity = activityMapper.selectById(registration.getActivityId());
        if (activity == null) {
            throw new BusinessException("活动不存在");
        }

        // 检查签到时间（活动开始前30分钟到活动开始后30分钟）
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime checkInStart = activity.getStartTime().minusMinutes(30);
        LocalDateTime checkInEnd = activity.getStartTime().plusMinutes(30);

        if (now.isBefore(checkInStart)) {
            throw new BusinessException("签到时间未到");
        }
        if (now.isAfter(checkInEnd)) {
            throw new BusinessException("签到时间已过");
        }

        registration.setStatus(RegistrationStatusEnum.CHECKED_IN.getCode());
        registration.setCheckInTime(now);

        String location = request.getLocation();
        if (location == null || location.isEmpty()) {
            location = activity.getLocation();
        }
        registration.setCheckInLocation(location);

        return this.updateById(registration);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean checkOut(CheckInRequest request) {
        Registration registration = this.getById(request.getRegistrationId());
        if (registration == null) {
            throw new BusinessException("报名记录不存在");
        }

        // 只能已签到的报名才能签出
        if (registration.getStatus() != RegistrationStatusEnum.CHECKED_IN.getCode()) {
            throw new BusinessException("请先签到");
        }

        // 检查活动状态
        Activity activity = activityMapper.selectById(registration.getActivityId());
        if (activity == null) {
            throw new BusinessException("活动不存在");
        }

        LocalDateTime now = LocalDateTime.now();

        registration.setStatus(RegistrationStatusEnum.CHECKED_OUT.getCode());
        registration.setCheckOutTime(now);

        String location = request.getLocation();
        if (location == null || location.isEmpty()) {
            location = activity.getLocation();
        }
        registration.setCheckOutLocation(location);

        boolean updated = this.updateById(registration);

        if (updated) {
            // 创建服务记录
            serviceRecordService.createServiceRecord(registration);
        }

        return updated;
    }

    @Override
    public RegistrationVO getRegistrationById(Long id) {
        Registration registration = this.getById(id);
        if (registration == null) {
            return null;
        }
        return convertToVO(registration);
    }

    @Override
    public Page<RegistrationVO> listRegistrations(RegistrationQueryRequest request) {
        QueryWrapper<Registration> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");

        if (request.getActivityId() != null) {
            queryWrapper.eq("activity_id", request.getActivityId());
        }
        if (request.getUserId() != null) {
            queryWrapper.eq("user_id", request.getUserId());
        }
        if (request.getStatus() != null) {
            queryWrapper.eq("status", request.getStatus());
        }

        Page<Registration> page = this.page(new Page<>(request.getCurrent(), request.getPageSize()), queryWrapper);

        List<RegistrationVO> voList = page.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        Page<RegistrationVO> voPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    public List<RegistrationVO> getUserRegistrations(Long userId) {
        List<Registration> list = baseMapper.selectByUserId(userId);
        return list.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RegistrationVO> getActivityRegistrations(Long activityId) {
        List<Registration> list = baseMapper.selectByActivityId(activityId);
        return list.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public void sendActivityReminder(Long registrationId) {
        Registration registration = this.getById(registrationId);
        if (registration == null) {
            return;
        }

        // 发送提醒通知（这里可以实现短信、推送等）
        log.info("发送活动提醒，报名ID：{}，用户ID：{}，活动ID：{}",
                registrationId, registration.getUserId(), registration.getActivityId());

        // 更新提醒状态
        registration.setReminderSent(1);
        registration.setReminderTime(LocalDateTime.now());
        this.updateById(registration);
    }

    @Override
    public List<Registration> getNeedReminderList() {
        // 查询活动开始前1小时到2小时之间的报名记录
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime = now.plusHours(1);
        LocalDateTime endTime = now.plusHours(2);
        return baseMapper.selectNeedReminderList(startTime, endTime);
    }

    @Override
    public boolean hasRegistered(Long activityId, Long userId) {
        QueryWrapper<Registration> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("activity_id", activityId)
                .eq("user_id", userId)
                .ne("status", RegistrationStatusEnum.CANCELLED.getCode())
                .ne("status", RegistrationStatusEnum.REJECTED.getCode());
        return this.count(queryWrapper) > 0;
    }

    /**
     * 转换为VO
     */
    private RegistrationVO convertToVO(Registration registration) {
        RegistrationVO vo = new RegistrationVO();
        BeanUtil.copyProperties(registration, vo);
        vo.setStatusDesc(RegistrationStatusEnum.getDescByCode(registration.getStatus()));

        // 加载活动信息
        Activity activity = activityMapper.selectById(registration.getActivityId());
        if (activity != null) {
            vo.setActivityTitle(activity.getTitle());
            vo.setActivityStartTime(activity.getStartTime());
            vo.setActivityLocation(activity.getLocation());
        }

        return vo;
    }
}
