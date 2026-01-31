package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.Notification;

import java.util.List;

/**
 * <p>
 * 通知表 服务类
 * </p>
 *
 * @author Rosy
 * @since 2025-01-31
 */
public interface INotificationService extends IService<Notification> {

    /**
     * 发送通知
     * @param title 通知标题
     * @param content 通知内容
     * @param type 通知类型
     * @param receiverId 接收者ID
     * @return 通知ID
     */
    Long sendNotification(String title, String content, Integer type, Long receiverId);

    /**
     * 发送系统通知给多个用户
     * @param title 通知标题
     * @param content 通知内容
     * @param receiverIds 接收者ID列表
     * @return 通知ID列表
     */
    List<Long> sendNotificationToUsers(String title, String content, Integer type, List<Long> receiverIds);

    /**
     * 标记通知为已读
     * @param notificationId 通知ID
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean markAsRead(Long notificationId, Long userId);

    /**
     * 批量标记通知为已读
     * @param notificationIds 通知ID列表
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean markAsReadBatch(List<Long> notificationIds, Long userId);

    /**
     * 获取用户未读通知数量
     * @param userId 用户ID
     * @return 未读通知数量
     */
    Long getUnreadCount(Long userId);

    /**
     * 分页获取用户通知列表
     * @param userId 用户ID
     * @param current 当前页
     * @param size 每页大小
     * @return 通知分页列表
     */
    Page<Notification> pageUserNotifications(Long userId, long current, long size);
}