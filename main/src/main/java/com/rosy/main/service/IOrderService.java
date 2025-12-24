package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.Order;

/**
 * 订单Service接口
 *
 * @author rosy
 */
public interface IOrderService extends IService<Order> {

    /**
     * 创建订单
     */
    Order createOrder(Order order);

    /**
     * 更新订单状态
     */
    boolean updateOrderStatus(Long orderId, Integer status);

    /**
     * 获取订单详情
     */
    Order getOrderDetail(Long orderId);
}