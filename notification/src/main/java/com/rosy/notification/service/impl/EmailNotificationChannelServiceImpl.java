package com.rosy.notification.service.impl;

import com.rosy.notification.domain.enums.NotificationTypeEnum;
import com.rosy.notification.service.INotificationChannelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 邮件通知渠道服务实现类
 */
@Slf4j
@Service("email")
public class EmailNotificationChannelServiceImpl implements INotificationChannelService {

    @Override
    public boolean sendNotification(String target, String content) {
        log.info("发送邮件通知: target={}, content={}", target, content);

        // 这里应该调用邮件API，比如Spring Boot Mail、阿里云邮件等
        // 示例代码，实际应该替换为真实的邮件发送逻辑
        try {
            // 模拟邮件发送成功
            log.info("邮件通知发送成功: target={}", target);
            return true;
        } catch (Exception e) {
            log.error("邮件通知发送失败: target={}", target, e);
            return false;
        }
    }

    @Override
    public String getNotificationType() {
        return NotificationTypeEnum.EMAIL.getCode();
    }
}