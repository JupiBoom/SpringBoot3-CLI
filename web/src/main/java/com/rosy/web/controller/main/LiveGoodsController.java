package com.rosy.web.controller.main;

import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.main.domain.vo.LiveGoodsVO;
import com.rosy.main.domain.vo.LiveGoodsAddVO;
import com.rosy.main.domain.vo.LiveGoodsBatchAddVO;
import com.rosy.main.service.ILiveGoodsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 直播间商品表 前端控制器
 * </p>
 *
 * @author Rosy
 * @since 2026-01-16
 */
@RestController
@RequestMapping("/live-goods")
@Tag(name = "直播间商品管理接口")
public class LiveGoodsController {

    private final ILiveGoodsService liveGoodsService;

    public LiveGoodsController(ILiveGoodsService liveGoodsService) {
        this.liveGoodsService = liveGoodsService;
    }

    @PostMapping
    @Operation(summary = "添加商品到直播间")
    public ApiResponse addGoodsToLive(@RequestBody LiveGoodsAddVO liveGoodsAddVO) {
        LiveGoodsVO liveGoodsVO = liveGoodsService.addGoodsToLive(liveGoodsAddVO);
        return ApiResponse.success(liveGoodsVO);
    }

    @PostMapping("/batch")
    @Operation(summary = "批量添加商品到直播间")
    public ApiResponse batchAddGoodsToLive(
            @RequestBody LiveGoodsBatchAddVO liveGoodsBatchAddVO) {
        List<LiveGoodsVO> liveGoodsVOList = liveGoodsService.batchAddGoodsToLive(liveGoodsBatchAddVO);
        return ApiResponse.success(liveGoodsVOList);
    }

    @GetMapping("/list/{liveRoomId}")
    @Operation(summary = "获取直播间商品列表")
    public ApiResponse getLiveGoodsList(@PathVariable Long liveRoomId) {
        List<LiveGoodsVO> liveGoodsVOList = liveGoodsService.getLiveGoodsList(liveRoomId);
        return ApiResponse.success(liveGoodsVOList);
    }

    @PutMapping("/{goodsId}/slogan")
    @Operation(summary = "更新商品卖点")
    public ApiResponse updateGoodsSlogan(
            @PathVariable Long goodsId,
            @RequestParam String slogan) {
        LiveGoodsVO liveGoodsVO = liveGoodsService.updateGoodsSlogan(goodsId, slogan);
        return ApiResponse.success(liveGoodsVO);
    }

    @PutMapping("/{goodsId}/sort")
    @Operation(summary = "更新商品排序")
    public ApiResponse updateGoodsSort(
            @PathVariable Long goodsId,
            @RequestParam Integer sortOrder) {
        LiveGoodsVO liveGoodsVO = liveGoodsService.updateGoodsSort(goodsId, sortOrder);
        return ApiResponse.success(liveGoodsVO);
    }
}
