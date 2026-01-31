package com.rosy.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.entity.Notification;
import com.rosy.main.enums.NotificationTypeEnum;
import com.rosy.main.mapper.NotificationMapper;
import com.rosy.main.service.INotificationService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 通知表 服务实现类
 * </p>
 *
 * @author Rosy
 * @since 2025-01-31
 */
@Service
public class NotificationServiceImpl extends ServiceImpl<NotificationMapper, Notification> implements INotificationService {

    @Override
    public Long sendNotification(String title, String content, Integer type, Long receiverId) {
        // 参数校验
        ThrowUtils.throwIf(title == null || title.trim().isEmpty(), ErrorCode.PARAMS_ERROR, "通知标题不能为空");
        ThrowUtils.throwIf(content == null || content.trim().isEmpty(), ErrorCode.PARAMS_ERROR, "通知内容不能为空");
        ThrowUtils.throwIf(receiverId == null, ErrorCode.PARAMS_ERROR, "接收者ID不能为空");

        // 创建通知
        Notification notification = new Notification();
        notification.setTitle(title);
        notification.setContent(content);
        notification.setType(type != null ? type.byteValue() : NotificationTypeEnum.SYSTEM.getCode());
        notification.setReceiverId(receiverId);
        notification.setIsRead((byte) 0); // 未读

        // 保存通知
        boolean result = this.save(notification);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "发送通知失败");

        return notification.getId();
    }

    @Override
    public List<Long> sendNotificationToUsers(String title, String content, Integer type, List<Long> receiverIds) {
        // 参数校验
        ThrowUtils.throwIf(title == null || title.trim().isEmpty(), new BusinessException("通知标题不能为空"));
        ThrowUtils.throwIf(content == null || content.trim().isEmpty(), new BusinessException("通知内容不能为空"));
        ThrowUtils.throwIf(receiverIds == null || receiverIds.isEmpty(), new BusinessException("接收者ID列表不能为空"));

        List<Long> notificationIds = new ArrayList<>();
        byte notificationType = type != null ? type.byteValue() : NotificationTypeEnum.SYSTEM.getCode();

        // 批量创建通知
        List<Notification> notifications = new ArrayList<>();
        for (Long receiverId : receiverIds) {
            Notification notification = new Notification();
            notification.setTitle(title);
            notification.setContent(content);
            notification.setType(notificationType);
            notification.setReceiverId(receiverId);
            notification.setIsRead((byte) 0); // 未读
            notifications.add(notification);
        }

        // 批量保存
        boolean result = this.saveBatch(notifications);
        ThrowUtils.throwIf(!result, new BusinessException("发送通知失败"));

        // 收集通知ID
        for (Notification notification : notifications) {
            notificationIds.add(notification.getId());
        }

        return notificationIds;
    }

    @Override
    public boolean markAsRead(Long notificationId, Long userId) {
        // 参数校验
        ThrowUtils.throwIf(notificationId == null, new BusinessException("通知ID不能为空"));
        ThrowUtils.throwIf(userId == null, new BusinessException("用户ID不能为空"));

        // 查询通知
        Notification notification = this.getById(notificationId);
        ThrowUtils.throwIf(notification == null, new BusinessException("通知不存在"));
        ThrowUtils.throwIf(!userId.equals(notification.getReceiverId()), new BusinessException("只能标记自己的通知为已读"));

        // 更新通知为已读
        UpdateWrapper<Notification> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", notificationId);
        updateWrapper.set("is_read", 1);

        return this.update(updateWrapper);
    }

    @Override
    public boolean markAsReadBatch(List<Long> notificationIds, Long userId) {
        // 参数校验
        ThrowUtils.throwIf(notificationIds == null || notificationIds.isEmpty(), new BusinessException("通知ID列表不能为空"));
        ThrowUtils.throwIf(userId == null, new BusinessException("用户ID不能为空"));

        // 更新通知为已读
        UpdateWrapper<Notification> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("id", notificationIds);
        updateWrapper.eq("receiver_id", userId);
        updateWrapper.set("is_read", 1);

        return this.update(updateWrapper);
    }

    @Override
    public Long getUnreadCount(Long userId) {
        // 参数校验
        ThrowUtils.throwIf(userId == null, new BusinessException("用户ID不能为空"));

        // 查询未读通知数量
        QueryWrapper<Notification> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("receiver_id", userId);
        queryWrapper.eq("is_read", 0);

        return this.count(queryWrapper);
    }

    @Override
    public Page<Notification> pageUserNotifications(Long userId, long current, long size) {
        // 参数校验
        ThrowUtils.throwIf(userId == null, new BusinessException("用户ID不能为空"));

        // 查询用户通知
        QueryWrapper<Notification> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("receiver_id", userId);
        queryWrapper.orderByDesc("create_time");

        return this.page(new Page<>(current, size), queryWrapper);
    }
}