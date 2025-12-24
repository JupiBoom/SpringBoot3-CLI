package com.rosy.main.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.OrderStatusEnum;
import com.rosy.main.domain.entity.Order;
import com.rosy.main.listener.OrderStatusChangeListener;
import com.rosy.main.mapper.OrderMapper;
import com.rosy.main.service.IOrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;

/**
 * 订单Service实现类
 *
 * @author rosy
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Order createOrder(Order order) {
        // 生成订单号
        order.setOrderNo(generateOrderNo());
        // 保存订单
        save(order);
        return order;
    }

    @Resource
    private OrderStatusChangeListener orderStatusChangeListener;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateOrderStatus(Long orderId, Integer status) {
        Order order = getById(orderId);
        if (order == null) {
            return false;
        }
        OrderStatusEnum newStatus = OrderStatusEnum.getByCode(status);
        if (newStatus == null) {
            return false;
        }
        order.setStatus(newStatus);
        boolean success = updateById(order);
        if (success) {
            // 触发订单状态变更监听
            orderStatusChangeListener.onOrderStatusChanged(order);
        }
        return success;
    }

    @Override
    public Order getOrderDetail(Long orderId) {
        return getById(orderId);
    }

    /**
     * 生成订单号
     */
    private String generateOrderNo() {
        // 简单的订单号生成逻辑，实际项目中可以使用更复杂的算法
        return System.currentTimeMillis() + "" + (int) (Math.random() * 9000 + 1000);
    }
}