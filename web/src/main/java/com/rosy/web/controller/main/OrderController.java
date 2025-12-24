package com.rosy.web.controller.main;

import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.main.domain.entity.Order;
import com.rosy.main.service.IOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;

/**
 * 订单控制器
 *
 * @author rosy
 */
@RestController
@RequestMapping("/order")
@Tag(name = "订单管理", description = "订单相关接口")
public class OrderController {

    @Resource
    private IOrderService orderService;

    @PostMapping("/create")
    @Operation(summary = "创建订单", description = "创建新的订单")
    public ApiResponse createOrder(@RequestBody Order order) {
        Order createdOrder = orderService.createOrder(order);
        return ApiResponse.success(createdOrder);
    }

    @PutMapping("/updateStatus")
    @Operation(summary = "更新订单状态", description = "更新订单状态")
    public ApiResponse updateOrderStatus(@RequestParam Long orderId, @RequestParam Integer status) {
        boolean success = orderService.updateOrderStatus(orderId, status);
        return ApiResponse.success(success);
    }

    @GetMapping("/detail/{orderId}")
    @Operation(summary = "获取订单详情", description = "根据订单ID获取订单详情")
    public ApiResponse getOrderDetail(@PathVariable Long orderId) {
        Order order = orderService.getOrderDetail(orderId);
        return ApiResponse.success(order);
    }
}