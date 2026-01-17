package com.rosy.repair.controller;

import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.repair.domain.request.*;
import com.rosy.repair.domain.vo.RepairOrderVO;
import com.rosy.repair.domain.vo.StatisticsVO;
import com.rosy.repair.service.IRepairOrderService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/repair/order")
public class RepairOrderController {

    private final IRepairOrderService repairOrderService;

    public RepairOrderController(IRepairOrderService repairOrderService) {
        this.repairOrderService = repairOrderService;
    }

    @PostMapping("/create")
    public ApiResponse createOrder(@RequestBody RepairOrderCreateRequest request) {
        Long userId = 1L;
        RepairOrderVO order = repairOrderService.createOrder(request, userId);
        return ApiResponse.success(order);
    }

    @PostMapping("/assign")
    public ApiResponse assignOrder(@RequestBody RepairOrderAssignRequest request) {
        repairOrderService.assignOrder(request);
        return ApiResponse.success();
    }

    @PostMapping("/auto-assign/{orderId}")
    public ApiResponse autoAssignOrder(@PathVariable Long orderId) {
        repairOrderService.autoAssignOrder(orderId);
        return ApiResponse.success();
    }

    @PostMapping("/accept/{orderId}")
    public ApiResponse acceptOrder(@PathVariable Long orderId) {
        Long repairerId = 1L;
        repairOrderService.acceptOrder(orderId, repairerId);
        return ApiResponse.success();
    }

    @PostMapping("/complete")
    public ApiResponse completeOrder(@RequestBody RepairRecordRequest request) {
        Long repairerId = 1L;
        repairOrderService.completeOrder(request, repairerId);
        return ApiResponse.success();
    }

    @PostMapping("/evaluate")
    public ApiResponse evaluateOrder(@RequestBody EvaluationRequest request) {
        Long userId = 1L;
        repairOrderService.evaluateOrder(request, userId);
        return ApiResponse.success();
    }

    @GetMapping("/detail/{orderId}")
    public ApiResponse getOrderDetail(@PathVariable Long orderId) {
        RepairOrderVO order = repairOrderService.getOrderDetail(orderId);
        return ApiResponse.success(order);
    }

    @GetMapping("/list")
    public ApiResponse getOrderList(
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long repairerId) {
        List<RepairOrderVO> list = repairOrderService.getOrderList(status, userId, repairerId);
        return ApiResponse.success(list);
    }

    @GetMapping("/statistics")
    public ApiResponse getStatistics(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        StatisticsVO statistics = repairOrderService.getStatistics(startTime, endTime);
        return ApiResponse.success(statistics);
    }
}