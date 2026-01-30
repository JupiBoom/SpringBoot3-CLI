package com.rosy.web.controller.main;

import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.dto.live.*;
import com.rosy.main.domain.vo.*;
import com.rosy.main.service.ILiveRoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/live/room")
@Tag(name = "直播间管理")
public class LiveRoomController {

    @Resource
    private ILiveRoomService liveRoomService;

    @PostMapping("/create")
    @ValidateRequest
    @Operation(summary = "创建直播间")
    public ApiResponse createRoom(@RequestBody LiveRoomCreateRequest request) {
        Long roomId = liveRoomService.createRoom(request);
        return ApiResponse.success(roomId);
    }

    @PostMapping("/update")
    @ValidateRequest
    @Operation(summary = "更新直播间信息")
    public ApiResponse updateRoom(@RequestBody LiveRoomUpdateRequest request) {
        if (request.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = liveRoomService.updateRoom(request);
        return ApiResponse.success(result);
    }

    @PostMapping("/update/status")
    @ValidateRequest
    @Operation(summary = "更新直播状态")
    public ApiResponse updateRoomStatus(@RequestBody LiveRoomStatusUpdateRequest request) {
        if (request.getRoomId() == null || request.getStatus() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = liveRoomService.updateRoomStatus(request);
        return ApiResponse.success(result);
    }

    @PostMapping("/delete")
    @ValidateRequest
    @Operation(summary = "删除直播间")
    public ApiResponse deleteRoom(@RequestBody IdRequest idRequest) {
        boolean result = liveRoomService.removeById(idRequest.getId());
        return ApiResponse.success(result);
    }

    @GetMapping("/get")
    @Operation(summary = "获取直播间详情")
    public ApiResponse getRoomDetail(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LiveRoomVO room = liveRoomService.getRoomDetail(id);
        return ApiResponse.success(room);
    }

    @GetMapping("/get/with-stats")
    @Operation(summary = "获取直播间详情（包含统计数据和商品列表）")
    public ApiResponse getRoomWithStats(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LiveRoomWithStatsVO room = liveRoomService.getRoomWithStats(id);
        return ApiResponse.success(room);
    }

    @PostMapping("/list")
    @ValidateRequest
    @Operation(summary = "获取直播间列表")
    public ApiResponse getRoomList(@RequestBody LiveRoomQueryRequest request) {
        List<LiveRoomVO> list = liveRoomService.getRoomList(request);
        return ApiResponse.success(list);
    }

    @PostMapping("/start")
    @ValidateRequest
    @Operation(summary = "开始直播")
    public ApiResponse startLive(@RequestBody IdRequest idRequest) {
        boolean result = liveRoomService.startLive(idRequest.getId());
        return ApiResponse.success(result);
    }

    @PostMapping("/end")
    @ValidateRequest
    @Operation(summary = "结束直播")
    public ApiResponse endLive(@RequestBody IdRequest idRequest) {
        boolean result = liveRoomService.endLive(idRequest.getId());
        return ApiResponse.success(result);
    }

    @PostMapping("/switch-product")
    @Operation(summary = "切换讲解商品")
    public ApiResponse switchExplainingProduct(Long roomId, Long productId) {
        if (roomId == null || productId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = liveRoomService.switchExplainingProduct(roomId, productId);
        return ApiResponse.success(result);
    }

    @GetMapping("/stats/conversion")
    @Operation(summary = "获取直播转化率统计")
    public ApiResponse getConversionStats(Long roomId) {
        if (roomId == null || roomId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LiveRoomConversionStatsVO stats = liveRoomService.getConversionStats(roomId);
        return ApiResponse.success(stats);
    }

    @GetMapping("/stats/retention")
    @Operation(summary = "获取观众留存曲线数据")
    public ApiResponse getRetentionCurve(Long roomId) {
        if (roomId == null || roomId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<LiveRetentionDataVO> retentionData = liveRoomService.getRetentionCurve(roomId);
        return ApiResponse.success(retentionData);
    }
}
