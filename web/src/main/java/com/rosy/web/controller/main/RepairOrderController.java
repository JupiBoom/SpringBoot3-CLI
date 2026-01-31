package com.rosy.web.controller.main;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.PageRequest;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.dto.RepairOrderAssignRequest;
import com.rosy.main.domain.dto.RepairOrderSubmitRequest;
import com.rosy.main.domain.vo.RepairOrderVO;
import com.rosy.main.service.IRepairOrderService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/repair/order")
public class RepairOrderController {

    @Resource
    private IRepairOrderService repairOrderService;

    @PostMapping("/submit")
    @ValidateRequest
    public ApiResponse submitOrder(@RequestBody RepairOrderSubmitRequest request) {
        RepairOrderVO orderVO = repairOrderService.submitOrder(request);
        return ApiResponse.success(orderVO);
    }

    @PostMapping("/assign")
    @ValidateRequest
    public ApiResponse assignOrder(@RequestBody RepairOrderAssignRequest request) {
        RepairOrderVO orderVO = repairOrderService.assignOrder(request);
        return ApiResponse.success(orderVO);
    }

    @PostMapping("/auto-assign/{orderId}")
    public ApiResponse autoAssignOrder(@PathVariable Long orderId) {
        if (orderId == null || orderId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        RepairOrderVO orderVO = repairOrderService.autoAssignOrder(orderId);
        return ApiResponse.success(orderVO);
    }

    @PostMapping("/accept/{orderId}")
    public ApiResponse acceptOrder(@PathVariable Long orderId) {
        if (orderId == null || orderId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        RepairOrderVO orderVO = repairOrderService.acceptOrder(orderId);
        return ApiResponse.success(orderVO);
    }

    @PostMapping("/complete/{orderId}")
    public ApiResponse completeOrder(@PathVariable Long orderId) {
        if (orderId == null || orderId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        RepairOrderVO orderVO = repairOrderService.completeOrder(orderId);
        return ApiResponse.success(orderVO);
    }

    @PostMapping("/confirm/{orderId}")
    public ApiResponse confirmCompletion(@PathVariable Long orderId) {
        if (orderId == null || orderId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        RepairOrderVO orderVO = repairOrderService.confirmCompletion(orderId);
        return ApiResponse.success(orderVO);
    }

    @PostMapping("/cancel/{orderId}")
    public ApiResponse cancelOrder(@PathVariable Long orderId) {
        if (orderId == null || orderId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        RepairOrderVO orderVO = repairOrderService.cancelOrder(orderId);
        return ApiResponse.success(orderVO);
    }

    @GetMapping("/detail/{orderId}")
    public ApiResponse getOrderDetail(@PathVariable Long orderId) {
        if (orderId == null || orderId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        RepairOrderVO orderVO = repairOrderService.getOrderDetail(orderId);
        return ApiResponse.success(orderVO);
    }

    @PostMapping("/my-orders")
    @ValidateRequest
    public ApiResponse getMyOrders(@RequestBody PageRequest pageRequest) {
        Page<RepairOrderVO> page = repairOrderService.getMyOrders(pageRequest);
        return ApiResponse.success(page);
    }

    @PostMapping("/staff-orders")
    @ValidateRequest
    public ApiResponse getStaffOrders(@RequestBody PageRequest pageRequest) {
        Page<RepairOrderVO> page = repairOrderService.getStaffOrders(pageRequest);
        return ApiResponse.success(page);
    }

    @PostMapping("/all-orders")
    @ValidateRequest
    public ApiResponse getAllOrders(@RequestBody PageRequest pageRequest) {
        Page<RepairOrderVO> page = repairOrderService.getAllOrders(pageRequest);
        return ApiResponse.success(page);
    }
}
