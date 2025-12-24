package com.rosy.main.service.impl;

import org.springframework.stereotype.Service;

/**
 * 微信通知服务实现类
 *
 * @author rosy
 */
@Service
public class WeChatNotificationService {

    public void send(Long userId, String content) {
        // 实际项目中需要集成微信服务号API
        System.out.println("发送微信通知给用户" + userId + ": " + content);
    }
}