package com.rosy.notification.service.impl;

import com.rosy.notification.domain.enums.NotificationTypeEnum;
import com.rosy.notification.service.INotificationChannelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 短信通知渠道服务实现类
 */
@Slf4j
@Service("sms")
public class SmsNotificationChannelServiceImpl implements INotificationChannelService {

    @Override
    public boolean sendNotification(String target, String content) {
        log.info("发送短信通知: target={}, content={}", target, content);

        // 这里应该调用短信API，比如阿里云短信、腾讯云短信等
        // 示例代码，实际应该替换为真实的短信发送逻辑
        try {
            // 模拟短信发送成功
            log.info("短信通知发送成功: target={}", target);
            return true;
        } catch (Exception e) {
            log.error("短信通知发送失败: target={}", target, e);
            return false;
        }
    }

    @Override
    public String getNotificationType() {
        return NotificationTypeEnum.SMS.getCode();
    }
}