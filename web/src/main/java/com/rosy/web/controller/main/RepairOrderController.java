package com.rosy.web.controller.main;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.dto.repair.RepairOrderAddRequest;
import com.rosy.main.domain.dto.repair.RepairOrderAssignRequest;
import com.rosy.main.domain.dto.repair.RepairOrderCompleteRequest;
import com.rosy.main.domain.dto.repair.RepairOrderQueryRequest;
import com.rosy.main.domain.vo.repair.RepairOrderVO;
import com.rosy.main.service.IRepairOrderService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/repair/order")
public class RepairOrderController {

    @Resource
    private IRepairOrderService repairOrderService;

    @PostMapping("/add")
    @ValidateRequest
    public ApiResponse createRepairOrder(@RequestBody RepairOrderAddRequest request, HttpServletRequest httpRequest) {
        Long userId = (Long) httpRequest.getAttribute("userId");
        RepairOrderVO vo = repairOrderService.createRepairOrder(request, userId);
        return ApiResponse.success(vo);
    }

    @PostMapping("/delete")
    @ValidateRequest
    public ApiResponse deleteRepairOrder(@RequestBody IdRequest request) {
        boolean result = repairOrderService.removeById(request.getId());
        return ApiResponse.success(result);
    }

    @GetMapping("/get")
    public ApiResponse getRepairOrderById(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        RepairOrderVO vo = repairOrderService.getRepairOrderVO(id);
        return ApiResponse.success(vo);
    }

    @PostMapping("/list/page")
    @ValidateRequest
    public ApiResponse listRepairOrdersByPage(@RequestBody RepairOrderQueryRequest request) {
        Page<RepairOrderVO> page = repairOrderService.pageRepairOrders(request);
        return ApiResponse.success(page);
    }

    @PostMapping("/assign")
    @ValidateRequest
    public ApiResponse assignOrder(@RequestBody RepairOrderAssignRequest request) {
        repairOrderService.assignOrder(request);
        return ApiResponse.success(true);
    }

    @PostMapping("/accept")
    public ApiResponse acceptOrder(@RequestParam Long orderId, HttpServletRequest httpRequest) {
        Long userId = (Long) httpRequest.getAttribute("userId");
        repairOrderService.acceptOrder(orderId, userId);
        return ApiResponse.success(true);
    }

    @PostMapping("/complete")
    @ValidateRequest
    public ApiResponse completeOrder(@RequestBody RepairOrderCompleteRequest request) {
        repairOrderService.completeOrder(request);
        return ApiResponse.success(true);
    }

    @PostMapping("/cancel")
    public ApiResponse cancelOrder(@RequestParam Long orderId, HttpServletRequest httpRequest) {
        Long userId = (Long) httpRequest.getAttribute("userId");
        repairOrderService.cancelOrder(orderId, userId);
        return ApiResponse.success(true);
    }
}
