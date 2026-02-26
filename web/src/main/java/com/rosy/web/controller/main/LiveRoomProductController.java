package com.rosy.web.controller.main;

import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.dto.liveroom.AudienceRecordRequest;
import com.rosy.main.domain.dto.liveroom.ExplainProductRequest;
import com.rosy.main.domain.dto.liveroom.LiveRoomProductAddRequest;
import com.rosy.main.domain.dto.liveroom.SalesRecordRequest;
import com.rosy.main.domain.vo.LiveRoomProductVO;
import com.rosy.main.service.IAudienceRecordService;
import com.rosy.main.service.ILiveRoomProductService;
import com.rosy.main.service.ISalesRecordService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/liveRoom/product")
public class LiveRoomProductController {

    @Resource
    private ILiveRoomProductService liveRoomProductService;

    @Resource
    private ISalesRecordService salesRecordService;

    @Resource
    private IAudienceRecordService audienceRecordService;

    @PostMapping("/add")
    @ValidateRequest
    public ApiResponse addProductToLiveRoom(@RequestBody LiveRoomProductAddRequest addRequest) {
        boolean result = liveRoomProductService.addProductToLiveRoom(
                addRequest.getLiveRoomId(),
                addRequest.getProductId(),
                addRequest.getSortOrder()
        );
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(true);
    }

    @PostMapping("/remove")
    @ValidateRequest
    public ApiResponse removeProductFromLiveRoom(@RequestBody LiveRoomProductAddRequest removeRequest) {
        boolean result = liveRoomProductService.removeProductFromLiveRoom(
                removeRequest.getLiveRoomId(),
                removeRequest.getProductId()
        );
        return ApiResponse.success(result);
    }

    @GetMapping("/list")
    public ApiResponse getLiveRoomProducts(Long liveRoomId) {
        if (liveRoomId == null || liveRoomId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<LiveRoomProductVO> products = liveRoomProductService.getLiveRoomProducts(liveRoomId);
        return ApiResponse.success(products);
    }

    @PostMapping("/explain")
    @ValidateRequest
    public ApiResponse setExplainingProduct(@RequestBody ExplainProductRequest explainRequest) {
        boolean result = liveRoomProductService.setExplainingProduct(
                explainRequest.getLiveRoomId(),
                explainRequest.getProductId()
        );
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(true);
    }

    @GetMapping("/explaining")
    public ApiResponse getCurrentExplainingProduct(Long liveRoomId) {
        if (liveRoomId == null || liveRoomId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LiveRoomProductVO product = liveRoomProductService.getCurrentExplainingProduct(liveRoomId);
        return ApiResponse.success(product);
    }

    @PostMapping("/sales/record")
    @ValidateRequest
    public ApiResponse recordSale(@RequestBody SalesRecordRequest salesRequest) {
        boolean result = salesRecordService.recordSale(
                salesRequest.getLiveRoomId(),
                salesRequest.getProductId(),
                salesRequest.getOrderNo(),
                salesRequest.getUserId(),
                salesRequest.getQuantity(),
                salesRequest.getUnitPrice()
        );
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(true);
    }

    @GetMapping("/sales/ranking")
    public ApiResponse getProductRanking(Long liveRoomId, Integer limit) {
        if (liveRoomId == null || liveRoomId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return ApiResponse.success(salesRecordService.getProductRanking(liveRoomId, limit));
    }

    @PostMapping("/audience/record")
    @ValidateRequest
    public ApiResponse recordAudience(@RequestBody AudienceRecordRequest audienceRequest) {
        boolean result = audienceRecordService.recordAudience(
                audienceRequest.getLiveRoomId(),
                audienceRequest.getViewerCount(),
                audienceRequest.getNewViewerCount(),
                audienceRequest.getLeaveViewerCount()
        );
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(true);
    }
}
