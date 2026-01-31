package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.Notification;

import java.util.List;

/**
 * 通知Service接口
 */
public interface INotificationService extends IService<Notification> {

    /**
     * 分页查询通知
     *
     * @param page    分页参数
     * @param userId  用户ID（可选）
     * @param type    通知类型（可选）
     * @param isRead  是否已读（可选）
     * @return 通知分页数据
     */
    Page<Notification> getNotificationPage(Page<Notification> page, Long userId, Integer type, Integer isRead);

    /**
     * 发送通知
     *
     * @param userId    接收用户ID
     * @param title     通知标题
     * @param content   通知内容
     * @param type      通知类型
     * @param relatedId 关联ID（可选）
     * @return 是否成功
     */
    boolean sendNotification(Long userId, String title, String content, Integer type, Long relatedId);

    /**
     * 批量发送通知
     *
     * @param userIds   接收用户ID列表
     * @param title     通知标题
     * @param content   通知内容
     * @param type      通知类型
     * @param relatedId 关联ID（可选）
     * @return 是否成功
     */
    boolean sendNotificationBatch(List<Long> userIds, String title, String content, Integer type, Long relatedId);

    /**
     * 标记通知为已读
     *
     * @param notificationId 通知ID
     * @return 是否成功
     */
    boolean markAsRead(Long notificationId);

    /**
     * 批量标记通知为已读
     *
     * @param notificationIds 通知ID列表
     * @return 是否成功
     */
    boolean markAsReadBatch(List<Long> notificationIds);

    /**
     * 标记用户所有通知为已读
     *
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean markAllAsRead(Long userId);

    /**
     * 获取用户未读通知数量
     *
     * @param userId 用户ID
     * @return 未读通知数量
     */
    long getUnreadCount(Long userId);
}