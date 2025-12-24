package com.rosy.main.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.main.domain.entity.NotificationRecord;
import com.rosy.main.mapper.NotificationRecordMapper;
import com.rosy.main.service.INotificationRecordService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 通知记录Service实现类
 *
 * @author rosy
 */
@Service
public class NotificationRecordServiceImpl extends ServiceImpl<NotificationRecordMapper, NotificationRecord> implements INotificationRecordService {

    @Override
    public void recordNotification(Long orderId, Long userId, String type, String channel, String content, Integer status, String errorMsg) {
        NotificationRecord record = new NotificationRecord();
        record.setOrderId(orderId);
        record.setUserId(userId);
        record.setType(type);
        record.setChannel(channel);
        record.setContent(content);
        record.setStatus(status);
        record.setErrorMsg(errorMsg);
        record.setRetryCount(0);
        save(record);
    }

    @Override
    @Scheduled(fixedRate = 60000) // 每分钟执行一次
    public void retryFailedNotifications() {
        // 查询所有发送失败且重试次数小于3次的通知
        List<NotificationRecord> failedNotifications = lambdaQuery()
                .eq(NotificationRecord::getStatus, 0)
                .lt(NotificationRecord::getRetryCount, 3)
                .list();

        for (NotificationRecord record : failedNotifications) {
            try {
                // 重试发送通知
                // 这里可以根据渠道调用相应的发送方法
                // 例如：如果是短信渠道，调用SmsNotificationService.send()
                // 实际项目中需要根据渠道类型进行不同的处理

                // 假设重试成功
                record.setStatus(1);
                record.setErrorMsg(null);
                record.setRetryCount(record.getRetryCount() + 1);
                record.setUpdateTime(LocalDateTime.now());
                updateById(record);
            } catch (Exception e) {
                // 重试失败，更新重试次数
                record.setRetryCount(record.getRetryCount() + 1);
                record.setErrorMsg(e.getMessage());
                record.setUpdateTime(LocalDateTime.now());
                updateById(record);
            }
        }
    }
}