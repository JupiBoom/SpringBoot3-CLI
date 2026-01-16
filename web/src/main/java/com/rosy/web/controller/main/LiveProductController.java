package com.rosy.web.controller.main;

import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.main.domain.dto.live.LiveProductAddRequest;
import com.rosy.main.domain.vo.LiveProductVO;
import com.rosy.main.service.ILiveProductService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 直播间商品管理控制器
 * </p>
 *
 * @author Rosy
 * @since 2026-01-16
 */
@RestController
@RequestMapping("/api/live/product")
public class LiveProductController {

    @Resource
    ILiveProductService liveProductService;

    @PostMapping("/add")
    public ApiResponse addProductToLive(@RequestBody LiveProductAddRequest request) {
        LiveProductVO liveProductVO = liveProductService.addProductToLive(request);
        return ApiResponse.success(liveProductVO);
    }

    @DeleteMapping("/remove/{id}")
    public ApiResponse removeProductFromLive(@PathVariable Long id) {
        boolean result = liveProductService.removeProductFromLive(id);
        return ApiResponse.success(result);
    }

    @PutMapping("/status/update")
    public ApiResponse updateProductStatus(@RequestParam Long id, @RequestParam Byte status) {
        boolean result = liveProductService.updateProductStatus(id, status);
        return ApiResponse.success(result);
    }

    @GetMapping("/list/{liveRoomId}")
    public ApiResponse listLiveProducts(@PathVariable Long liveRoomId) {
        List<LiveProductVO> products = liveProductService.listLiveProducts(liveRoomId);
        return ApiResponse.success(products);
    }

    @PostMapping("/sort/update")
    public ApiResponse updateProductSortOrder(@RequestParam Long id, @RequestParam Integer sortOrder) {
        boolean result = liveProductService.updateProductSortOrder(id, sortOrder);
        return ApiResponse.success(result);
    }
}
