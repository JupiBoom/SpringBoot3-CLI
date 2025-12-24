package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.NotificationRecord;

/**
 * 通知记录Service接口
 *
 * @author rosy
 */
public interface INotificationRecordService extends IService<NotificationRecord> {

    /**
     * 记录通知发送情况
     */
    void recordNotification(Long orderId, Long userId, String type, String channel, String content, Integer status, String errorMsg);

    /**
     * 重试发送失败的通知
     */
    void retryFailedNotifications();
}