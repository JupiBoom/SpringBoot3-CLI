package com.rosy.web.controller.main;

import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.entity.LiveRoomProduct;
import com.rosy.main.domain.vo.LiveRoomProductVO;
import com.rosy.main.service.ILiveRoomProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/liveRoomProduct")
@Tag(name = "直播间商品管理", description = "直播间商品相关接口")
public class LiveRoomProductController {

    @Resource
    private ILiveRoomProductService liveRoomProductService;

    @PostMapping("/add")
    @Operation(summary = "添加商品到直播间")
    @ValidateRequest
    public ApiResponse addProduct(@RequestBody LiveRoomProduct liveRoomProduct) {
        ThrowUtils.throwIf(liveRoomProduct == null, ErrorCode.PARAMS_ERROR);
        LiveRoomProductVO result = liveRoomProductService.addProduct(liveRoomProduct);
        return ApiResponse.success(result);
    }

    @PostMapping("/remove")
    @Operation(summary = "从直播间移除商品")
    @ValidateRequest
    public ApiResponse removeProduct(@RequestBody IdRequest idRequest) {
        ThrowUtils.throwIf(idRequest == null || idRequest.getId() == null, ErrorCode.PARAMS_ERROR);
        boolean result = liveRoomProductService.removeProduct(idRequest.getId());
        return ApiResponse.success(result);
    }

    @PostMapping("/update")
    @Operation(summary = "更新直播间商品")
    @ValidateRequest
    public ApiResponse updateProduct(@RequestBody LiveRoomProduct liveRoomProduct) {
        ThrowUtils.throwIf(liveRoomProduct == null || liveRoomProduct.getId() == null, ErrorCode.PARAMS_ERROR);
        LiveRoomProductVO result = liveRoomProductService.updateProduct(liveRoomProduct);
        return ApiResponse.success(result);
    }

    @GetMapping("/list")
    @Operation(summary = "获取直播间商品列表")
    public ApiResponse getProductsByLiveRoomId(@RequestParam Long liveRoomId) {
        ThrowUtils.throwIf(liveRoomId == null || liveRoomId <= 0, ErrorCode.PARAMS_ERROR);
        List<LiveRoomProductVO> result = liveRoomProductService.getProductsByLiveRoomId(liveRoomId);
        return ApiResponse.success(result);
    }

    @PostMapping("/switch")
    @Operation(summary = "切换讲解商品")
    @ValidateRequest
    public ApiResponse switchCurrentProduct(@RequestBody SwitchProductRequest request) {
        ThrowUtils.throwIf(request == null || request.getLiveRoomId() == null || request.getProductId() == null, ErrorCode.PARAMS_ERROR);
        boolean result = liveRoomProductService.switchCurrentProduct(request.getLiveRoomId(), request.getProductId());
        return ApiResponse.success(result);
    }

    public static class SwitchProductRequest {
        private Long liveRoomId;
        private Long productId;

        public Long getLiveRoomId() {
            return liveRoomId;
        }

        public void setLiveRoomId(Long liveRoomId) {
            this.liveRoomId = liveRoomId;
        }

        public Long getProductId() {
            return productId;
        }

        public void setProductId(Long productId) {
            this.productId = productId;
        }
    }
}