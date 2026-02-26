package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.enums.NotificationTypeEnum;
import com.rosy.common.enums.RegistrationStatusEnum;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.entity.Activity;
import com.rosy.main.domain.entity.Notification;
import com.rosy.main.domain.entity.Registration;
import com.rosy.main.domain.vo.NotificationVO;
import com.rosy.main.mapper.ActivityMapper;
import com.rosy.main.mapper.NotificationMapper;
import com.rosy.main.mapper.RegistrationMapper;
import com.rosy.main.service.INotificationService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl extends ServiceImpl<NotificationMapper, Notification> implements INotificationService {

    @Resource
    private ActivityMapper activityMapper;

    @Resource
    private RegistrationMapper registrationMapper;

    @Override
    public NotificationVO getNotificationVO(Notification notification) {
        if (notification == null) {
            return null;
        }
        NotificationVO vo = BeanUtil.copyProperties(notification, NotificationVO.class);

        NotificationTypeEnum typeEnum = NotificationTypeEnum.getByCode(notification.getType());
        if (typeEnum != null) {
            vo.setTypeDesc(typeEnum.getDesc());
        }

        return vo;
    }

    @Override
    public void sendActivityReminder(Long activityId, Long userId) {
        Activity activity = activityMapper.selectById(activityId);
        if (activity == null) {
            return;
        }

        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setTitle("活动提醒");
        notification.setContent(String.format("您报名的活动【%s】将于1小时后开始，请准时参加。活动地点：%s",
                activity.getTitle(), activity.getLocation()));
        notification.setType((byte) NotificationTypeEnum.ACTIVITY_REMINDER.getCode());
        notification.setRelatedId(activityId);

        this.save(notification);
    }

    @Override
    public void sendRegistrationAuditNotice(Long registrationId, boolean approved) {
        Registration registration = registrationMapper.selectById(registrationId);
        if (registration == null) {
            return;
        }

        Activity activity = activityMapper.selectById(registration.getActivityId());
        if (activity == null) {
            return;
        }

        Notification notification = new Notification();
        notification.setUserId(registration.getUserId());
        notification.setTitle("报名审核通知");
        notification.setContent(String.format("您报名的活动【%s】审核%s。%s",
                activity.getTitle(),
                approved ? "已通过" : "未通过",
                approved ? "请准时参加活动。" : "感谢您的关注，期待下次参与。"));
        notification.setType((byte) NotificationTypeEnum.REGISTRATION_AUDIT.getCode());
        notification.setRelatedId(registrationId);

        this.save(notification);
    }

    @Override
    public void sendSystemNotice(Long userId, String title, String content) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setTitle(title);
        notification.setContent(content);
        notification.setType((byte) NotificationTypeEnum.SYSTEM.getCode());

        this.save(notification);
    }

    @Override
    public List<NotificationVO> getUnreadNotifications(Long userId) {
        LambdaQueryWrapper<Notification> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Notification::getUserId, userId)
                .eq(Notification::getIsRead, (byte) 0)
                .orderByDesc(Notification::getCreateTime);

        return this.list(queryWrapper).stream()
                .map(this::getNotificationVO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean markAsRead(Long notificationId) {
        Notification notification = this.getById(notificationId);
        if (notification == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "通知不存在");
        }

        notification.setIsRead((byte) 1);
        notification.setReadTime(LocalDateTime.now());

        return this.updateById(notification);
    }

    @Override
    public boolean markAllAsRead(Long userId) {
        LambdaUpdateWrapper<Notification> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Notification::getUserId, userId)
                .eq(Notification::getIsRead, (byte) 0)
                .set(Notification::getIsRead, (byte) 1)
                .set(Notification::getReadTime, LocalDateTime.now());

        return this.update(updateWrapper);
    }
}
