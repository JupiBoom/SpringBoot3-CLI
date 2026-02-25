package com.rosy.web.controller.main;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.dto.live.LiveOrderAddRequest;
import com.rosy.main.domain.vo.LiveOrderVO;
import com.rosy.main.service.ILiveOrderService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 直播间订单管理 前端控制器
 * </p>
 *
 * @author Rosy
 * @since 2025-02-25
 */
@RestController
@RequestMapping("/live/order")
public class LiveOrderController {

    @Resource
    private ILiveOrderService liveOrderService;

    /**
     * 创建订单
     */
    @PostMapping("/create")
    @ValidateRequest
    public ApiResponse createOrder(@RequestBody LiveOrderAddRequest request) {
        Long orderId = liveOrderService.createOrder(request);
        return ApiResponse.success(orderId);
    }

    /**
     * 支付订单
     */
    @PostMapping("/pay")
    @ValidateRequest
    public ApiResponse payOrder(@RequestBody IdRequest idRequest) {
        boolean result = liveOrderService.payOrder(idRequest.getId());
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(true);
    }

    /**
     * 取消订单
     */
    @PostMapping("/cancel")
    @ValidateRequest
    public ApiResponse cancelOrder(@RequestBody IdRequest idRequest) {
        boolean result = liveOrderService.cancelOrder(idRequest.getId());
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(true);
    }

    /**
     * 根据ID获取订单
     */
    @GetMapping("/get")
    public ApiResponse getOrderById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LiveOrderVO orderVO = liveOrderService.getOrderById(id);
        ThrowUtils.throwIf(orderVO == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(orderVO);
    }

    /**
     * 分页查询直播间订单
     */
    @GetMapping("/list")
    public ApiResponse listOrdersByRoomId(long roomId, int current, int size) {
        if (roomId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Page<LiveOrderVO> page = liveOrderService.listOrdersByRoomId(roomId, current, size);
        return ApiResponse.success(page);
    }

    /**
     * 查询直播间订单总金额
     */
    @GetMapping("/totalAmount")
    public ApiResponse getTotalSalesAmount(long roomId) {
        if (roomId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        BigDecimal amount = liveOrderService.getTotalSalesAmount(roomId);
        return ApiResponse.success(amount);
    }

    /**
     * 查询直播间各商品销售统计
     */
    @GetMapping("/itemStats")
    public ApiResponse getItemSalesStats(long roomId) {
        if (roomId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<Map<String, Object>> stats = liveOrderService.getItemSalesStats(roomId);
        return ApiResponse.success(stats);
    }
}
