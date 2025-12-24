package com.rosy.main.service;

import com.rosy.main.domain.entity.Order;

/**
 * 通知服务接口
 *
 * @author rosy
 */
public interface INotificationService {

    /**
     * 发送支付成功通知
     */
    void sendPaymentSuccessNotification(Order order);

    /**
     * 发送发货通知
     */
    void sendShipmentNotification(Order order);

    /**
     * 发送签收通知
     */
    void sendReceiptNotification(Order order);
}