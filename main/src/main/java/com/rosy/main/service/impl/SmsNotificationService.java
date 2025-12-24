package com.rosy.main.service.impl;

import org.springframework.stereotype.Service;

/**
 * 短信通知服务实现类
 *
 * @author rosy
 */
@Service
public class SmsNotificationService {

    public void send(Long userId, String content) {
        // 实际项目中需要集成短信服务API
        System.out.println("发送短信通知给用户" + userId + ": " + content);
    }
}