package com.rosy.notification.service;

/**
 * 通知渠道服务接口
 */
public interface INotificationChannelService {
    /**
     * 发送通知
     */
    boolean sendNotification(String target, String content);

    /**
     * 获取通知类型
     */
    String getNotificationType();
}