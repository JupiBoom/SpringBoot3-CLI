package com.rosy.web.controller.main;

import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.dto.liveRoom.LiveRoomProductAddRequest;
import com.rosy.main.domain.dto.liveRoom.SwitchCurrentProductRequest;
import com.rosy.main.domain.vo.LiveRoomProductVO;
import com.rosy.main.domain.vo.ProductSalesRankingVO;
import com.rosy.main.service.ILiveRoomProductService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 直播间商品关联表 前端控制器
 * </p>
 *
 * @author Rosy
 * @since 2025-01-30
 */
@RestController
@RequestMapping("/live-room-product")
public class LiveRoomProductController {
    @Resource
    ILiveRoomProductService liveRoomProductService;

    /**
     * 添加商品到直播间
     */
    @PostMapping("/add")
    @ValidateRequest
    public ApiResponse addProductToLiveRoom(@RequestBody LiveRoomProductAddRequest liveRoomProductAddRequest) {
        boolean result = liveRoomProductService.addProductToLiveRoom(liveRoomProductAddRequest);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(true);
    }

    /**
     * 从直播间移除商品
     */
    @PostMapping("/remove")
    @ValidateRequest
    public ApiResponse removeProductFromLiveRoom(@RequestBody IdRequest idRequest) {
        boolean result = liveRoomProductService.removeProductFromLiveRoom(idRequest.getId());
        return ApiResponse.success(result);
    }

    /**
     * 切换当前讲解商品
     */
    @PostMapping("/switch-current")
    @ValidateRequest
    public ApiResponse switchCurrentProduct(@RequestBody SwitchCurrentProductRequest switchCurrentProductRequest) {
        boolean result = liveRoomProductService.switchCurrentProduct(switchCurrentProductRequest);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(true);
    }

    /**
     * 获取直播间商品列表
     */
    @GetMapping("/list/{liveRoomId}")
    public ApiResponse getProductsByLiveRoomId(@PathVariable Long liveRoomId) {
        if (liveRoomId == null || liveRoomId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<LiveRoomProductVO> products = liveRoomProductService.getProductsByLiveRoomId(liveRoomId);
        return ApiResponse.success(products);
    }

    /**
     * 获取商品销售排行榜
     */
    @GetMapping("/sales-ranking/{liveRoomId}")
    public ApiResponse getProductSalesRanking(
            @PathVariable Long liveRoomId,
            @RequestParam(defaultValue = "10") Integer limit) {
        if (liveRoomId == null || liveRoomId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<ProductSalesRankingVO> ranking = liveRoomProductService.getProductSalesRanking(liveRoomId, limit);
        return ApiResponse.success(ranking);
    }
}