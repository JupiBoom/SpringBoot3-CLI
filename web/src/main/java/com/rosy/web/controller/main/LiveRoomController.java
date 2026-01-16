package com.rosy.web.controller.main;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.entity.LiveRoom;
import com.rosy.main.domain.vo.LiveRoomVO;
import com.rosy.main.service.ILiveRoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/liveRoom")
@Tag(name = "直播间管理", description = "直播间相关接口")
public class LiveRoomController {

    @Resource
    private ILiveRoomService liveRoomService;

    @PostMapping("/create")
    @Operation(summary = "创建直播间")
    @ValidateRequest
    public ApiResponse createLiveRoom(@RequestBody LiveRoom liveRoom) {
        ThrowUtils.throwIf(liveRoom == null, ErrorCode.PARAMS_ERROR);
        LiveRoomVO result = liveRoomService.createLiveRoom(liveRoom);
        return ApiResponse.success(result);
    }

    @PostMapping("/update")
    @Operation(summary = "更新直播间")
    @ValidateRequest
    public ApiResponse updateLiveRoom(@RequestBody LiveRoom liveRoom) {
        ThrowUtils.throwIf(liveRoom == null || liveRoom.getId() == null, ErrorCode.PARAMS_ERROR);
        LiveRoomVO result = liveRoomService.updateLiveRoom(liveRoom);
        return ApiResponse.success(result);
    }

    @PostMapping("/delete")
    @Operation(summary = "删除直播间")
    @ValidateRequest
    public ApiResponse deleteLiveRoom(@RequestBody IdRequest idRequest) {
        ThrowUtils.throwIf(idRequest == null || idRequest.getId() == null, ErrorCode.PARAMS_ERROR);
        boolean result = liveRoomService.deleteLiveRoom(idRequest.getId());
        return ApiResponse.success(result);
    }

    @GetMapping("/detail")
    @Operation(summary = "获取直播间详情")
    public ApiResponse getLiveRoomDetail(@RequestParam Long id) {
        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR);
        LiveRoomVO result = liveRoomService.getLiveRoomDetail(id);
        return ApiResponse.success(result);
    }

    @GetMapping("/list")
    @Operation(summary = "获取直播间列表")
    public ApiResponse getLiveRoomList(@RequestParam(required = false) Long anchorId,
                                        @RequestParam(required = false) Byte status) {
        List<LiveRoomVO> result = liveRoomService.getLiveRoomList(anchorId, status);
        return ApiResponse.success(result);
    }

    @PostMapping("/start")
    @Operation(summary = "开始直播")
    @ValidateRequest
    public ApiResponse startLive(@RequestBody IdRequest idRequest) {
        ThrowUtils.throwIf(idRequest == null || idRequest.getId() == null, ErrorCode.PARAMS_ERROR);
        boolean result = liveRoomService.startLive(idRequest.getId());
        return ApiResponse.success(result);
    }

    @PostMapping("/end")
    @Operation(summary = "结束直播")
    @ValidateRequest
    public ApiResponse endLive(@RequestBody IdRequest idRequest) {
        ThrowUtils.throwIf(idRequest == null || idRequest.getId() == null, ErrorCode.PARAMS_ERROR);
        boolean result = liveRoomService.endLive(idRequest.getId());
        return ApiResponse.success(result);
    }
}