package com.rosy.notification.listener;

import com.rosy.notification.domain.entity.OrderStatusChangeEvent;
import com.rosy.notification.service.INotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 订单状态变更事件监听器
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderStatusChangeListener {

    private final INotificationService notificationService;

    @RabbitListener(queues = "order.status.change.queue")
    public void onOrderStatusChange(OrderStatusChangeEvent event) {
        log.info("接收到订单状态变更事件: orderId={}, status={}", event.getOrderId(), event.getOrderStatus());
        notificationService.handleOrderStatusChangeEvent(event);
    }
}