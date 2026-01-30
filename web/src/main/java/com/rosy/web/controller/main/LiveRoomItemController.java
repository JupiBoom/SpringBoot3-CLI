package com.rosy.web.controller.main;

import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.dto.live.LiveRoomItemAddRequest;
import com.rosy.main.domain.dto.live.LiveRoomItemUpdateRequest;
import com.rosy.main.domain.entity.LiveRoomItem;
import com.rosy.main.domain.entity.LiveRoomItemRank;
import com.rosy.main.domain.vo.LiveRoomItemVO;
import com.rosy.main.service.ILiveRoomItemRankService;
import com.rosy.main.service.ILiveRoomItemService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/live/room/item")
public class LiveRoomItemController {

    @Resource
    ILiveRoomItemService liveRoomItemService;

    @Resource
    ILiveRoomItemRankService liveRoomItemRankService;

    @PostMapping("/add")
    public ApiResponse addLiveRoomItem(@RequestBody LiveRoomItemAddRequest liveRoomItemAddRequest) {
        boolean result = liveRoomItemService.addLiveRoomItem(
                liveRoomItemAddRequest.getLiveRoomId(),
                liveRoomItemAddRequest.getItemId(),
                liveRoomItemAddRequest.getSellingPoints(),
                liveRoomItemAddRequest.getSortOrder()
        );
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success();
    }

    @PostMapping("/update")
    public ApiResponse updateLiveRoomItem(@RequestBody LiveRoomItemUpdateRequest liveRoomItemUpdateRequest) {
        LiveRoomItem liveRoomItem = new LiveRoomItem();
        BeanUtils.copyProperties(liveRoomItemUpdateRequest, liveRoomItem);
        boolean result = liveRoomItemService.updateById(liveRoomItem);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success();
    }

    @PostMapping("/delete")
    public ApiResponse deleteLiveRoomItem(@RequestParam Long liveRoomId, @RequestParam Long itemId) {
        boolean result = liveRoomItemService.removeLiveRoomItem(liveRoomId, itemId);
        return ApiResponse.success(result);
    }

    @GetMapping("/list/{liveRoomId}")
    public ApiResponse getLiveRoomItems(@PathVariable Long liveRoomId) {
        List<LiveRoomItemVO> liveRoomItems = liveRoomItemService.getLiveRoomItems(liveRoomId);
        return ApiResponse.success(liveRoomItems);
    }

    @GetMapping("/rank/{liveRoomId}")
    public ApiResponse getLiveRoomItemRank(@PathVariable Long liveRoomId) {
        List<LiveRoomItemRank> itemRankList = liveRoomItemRankService.getLiveRoomItemRank(liveRoomId);
        return ApiResponse.success(itemRankList);
    }

    @PostMapping("/rank/update")
    public ApiResponse updateLiveRoomItemRank(@RequestParam Long liveRoomId) {
        boolean result = liveRoomItemRankService.updateItemRank(liveRoomId);
        return ApiResponse.success(result);
    }
}