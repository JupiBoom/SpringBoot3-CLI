package com.rosy.web.controller.main;

import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.main.domain.vo.LiveRoomVO;
import com.rosy.main.domain.vo.LiveRoomCreateVO;
import com.rosy.main.service.ILiveRoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 直播间表 前端控制器
 * </p>
 *
 * @author Rosy
 * @since 2026-01-16
 */
@RestController
@RequestMapping("/live-room")
@Tag(name = "直播间管理接口")
public class LiveRoomController {

    private final ILiveRoomService liveRoomService;

    public LiveRoomController(ILiveRoomService liveRoomService) {
        this.liveRoomService = liveRoomService;
    }

    @PostMapping
    @Operation(summary = "创建直播间")
    public ApiResponse createLiveRoom(@RequestBody LiveRoomCreateVO liveRoomCreateVO) {
        LiveRoomVO liveRoomVO = liveRoomService.createLiveRoom(liveRoomCreateVO);
        return ApiResponse.success(liveRoomVO);
    }

    @PostMapping("/{roomId}/start")
    @Operation(summary = "开始直播")
    public ApiResponse startLive(@PathVariable Long roomId) {
        LiveRoomVO liveRoomVO = liveRoomService.startLive(roomId);
        return ApiResponse.success(liveRoomVO);
    }

    @PostMapping("/{roomId}/end")
    @Operation(summary = "结束直播")
    public ApiResponse endLive(@PathVariable Long roomId) {
        LiveRoomVO liveRoomVO = liveRoomService.endLive(roomId);
        return ApiResponse.success(liveRoomVO);
    }

    @PostMapping("/{roomId}/switch-goods")
    @Operation(summary = "切换讲解商品")
    public ApiResponse switchCurrentGoods(
            @PathVariable Long roomId,
            @RequestParam Long goodsId) {
        LiveRoomVO liveRoomVO = liveRoomService.switchCurrentGoods(roomId, goodsId);
        return ApiResponse.success(liveRoomVO);
    }

    @GetMapping("/{roomId}")
    @Operation(summary = "获取直播间详情")
    public ApiResponse getLiveRoomDetail(@PathVariable Long roomId) {
        LiveRoomVO liveRoomVO = liveRoomService.getLiveRoomDetail(roomId);
        return ApiResponse.success(liveRoomVO);
    }
}
