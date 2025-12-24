package com.rosy.notification;

import com.rosy.notification.domain.entity.OrderStatusChangeEvent;
import com.rosy.notification.domain.entity.NotificationRecord;
import com.rosy.notification.domain.entity.UserNotificationPreference;
import com.rosy.notification.domain.entity.NotificationTemplate;
import com.rosy.notification.domain.enums.NotificationTypeEnum;
import com.rosy.notification.domain.enums.OrderStatusEnum;
import com.rosy.notification.service.INotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 通知服务测试类
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = NotificationApplication.class)
public class NotificationServiceTest {

    @Autowired
    private INotificationService notificationService;

    @Test
    public void testHandleOrderStatusChangeEvent() {
        // 创建订单状态变更事件
        OrderStatusChangeEvent event = new OrderStatusChangeEvent();
        event.setEventId("event_123");
        event.setOrderId("order_123456");
        event.setProductName("测试商品");
        event.setOrderAmount(99.99);
        event.setOrderStatus(OrderStatusEnum.PAY_SUCCESS.getCode());
        event.setUserId("user_1");
        event.setUserPhone("13800138000");
        event.setUserEmail("user1@example.com");
        event.setUserWechatOpenid("openid_123");
        event.setEventTime(LocalDateTime.now());

        // 处理订单状态变更事件
        notificationService.handleOrderStatusChangeEvent(event);
    }

    @Test
    public void testReplaceTemplateVariables() {
        String templateContent = "您的订单{{orderId}}已支付成功，商品{{productName}}，金额{{orderAmount}}元。";
        Map<String, Object> variables = Map.of(
                "orderId", "order_123456",
                "productName", "测试商品",
                "orderAmount", 99.99
        );

        String content = notificationService.replaceTemplateVariables(templateContent, variables);
        System.out.println("替换后的内容: " + content);
    }

    @Test
    public void testGetUserNotificationPreferences() {
        List<UserNotificationPreference> preferences = notificationService.getUserNotificationPreferences("user_1");
        System.out.println("用户通知偏好设置: " + preferences);
    }

    @Test
    public void testGetNotificationTemplate() {
        NotificationTemplate template = notificationService.getNotificationTemplate(NotificationTypeEnum.SMS.getCode(), OrderStatusEnum.PAY_SUCCESS.getCode());
        System.out.println("通知模板: " + template);
    }
}