package com.rosy.main.listener;

import com.rosy.main.domain.entity.Order;
import com.rosy.main.service.INotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 订单状态变更监听器
 *
 * @author rosy
 */
@Component
public class OrderStatusChangeListener {

    @Autowired
    private INotificationService notificationService;

    /**
     * 订单状态变更时触发
     */
    public void onOrderStatusChanged(Order order) {
        // 识别关键节点（支付成功/发货/签收）
        switch (order.getStatus()) {
            case PAID:
                // 支付成功通知
                notificationService.sendPaymentSuccessNotification(order);
                break;
            case SHIPPED:
                // 发货通知
                notificationService.sendShipmentNotification(order);
                break;
            case RECEIVED:
                // 签收通知
                notificationService.sendReceiptNotification(order);
                break;
            default:
                // 其他状态不处理
                break;
        }
    }
}