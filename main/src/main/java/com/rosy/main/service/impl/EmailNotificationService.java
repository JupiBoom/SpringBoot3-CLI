package com.rosy.main.service.impl;

import org.springframework.stereotype.Service;

/**
 * 邮件通知服务实现类
 *
 * @author rosy
 */
@Service
public class EmailNotificationService {

    public void send(Long userId, String content) {
        // 实际项目中需要集成邮件服务API
        System.out.println("发送邮件通知给用户" + userId + ": " + content);
    }
}