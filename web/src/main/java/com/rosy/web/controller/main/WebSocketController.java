package com.rosy.web.controller.main;

import com.rosy.main.domain.vo.NotificationVO;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * WebSocket消息控制器
 */
@RestController
@RequestMapping("/api/websocket")
public class WebSocketController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    /**
     * 发送通知给指定用户
     */
    @PostMapping("/send-notification")
    public void sendNotification(@RequestBody NotificationVO notification) {
        // 发送到用户的私有队列
        messagingTemplate.convertAndSendToUser(
            String.valueOf(notification.getUserId()),
            "/queue/notifications",
            notification
        );
    }

    /**
     * 发送工单状态更新通知
     */
    @PostMapping("/send-order-update")
    public void sendOrderUpdate(
            @RequestParam Long userId,
            @RequestBody Map<String, Object> orderUpdate) {
        messagingTemplate.convertAndSendToUser(
            String.valueOf(userId),
            "/queue/order-updates",
            orderUpdate
        );
    }

    /**
     * 广播通知给所有维修人员
     */
    @PostMapping("/broadcast-to-repairmen")
    public void broadcastToRepairmen(@RequestBody Map<String, Object> message) {
        messagingTemplate.convertAndSend("/topic/repairmen", message);
    }

    /**
     * 用户订阅通知
     */
    @MessageMapping("/subscribe/notifications")
    @SendTo("/queue/notifications")
    public NotificationVO subscribeNotifications(NotificationVO notification) {
        return notification;
    }
}