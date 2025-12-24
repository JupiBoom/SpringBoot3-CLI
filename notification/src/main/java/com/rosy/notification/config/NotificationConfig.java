package com.rosy.notification.config;

import com.rosy.notification.service.INotificationChannelService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 通知服务配置类
 */
@Configuration
public class NotificationConfig {

    @Bean
    public Map<String, INotificationChannelService> notificationChannelServices(Map<String, INotificationChannelService> channelServices) {
        // 将所有实现了INotificationChannelService接口的bean注册到map中，key为bean名称
        return channelServices;
    }
}