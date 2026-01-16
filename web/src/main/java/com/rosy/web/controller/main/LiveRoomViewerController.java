package com.rosy.web.controller.main;

import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.entity.LiveRoomViewer;
import com.rosy.main.domain.vo.LiveRoomViewerVO;
import com.rosy.main.service.ILiveRoomViewerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/liveRoomViewer")
@Tag(name = "观众管理", description = "观众相关接口")
public class LiveRoomViewerController {

    @Resource
    private ILiveRoomViewerService liveRoomViewerService;

    @PostMapping("/entry")
    @Operation(summary = "记录观众进入")
    @ValidateRequest
    public ApiResponse recordViewerEntry(@RequestBody LiveRoomViewer liveRoomViewer) {
        ThrowUtils.throwIf(liveRoomViewer == null, ErrorCode.PARAMS_ERROR);
        LiveRoomViewerVO result = liveRoomViewerService.recordViewerEntry(liveRoomViewer);
        return ApiResponse.success(result);
    }

    @PostMapping("/exit")
    @Operation(summary = "记录观众离开")
    @ValidateRequest
    public ApiResponse recordViewerExit(@RequestBody IdRequest idRequest) {
        ThrowUtils.throwIf(idRequest == null || idRequest.getId() == null, ErrorCode.PARAMS_ERROR);
        boolean result = liveRoomViewerService.recordViewerExit(idRequest.getId());
        return ApiResponse.success(result);
    }

    @GetMapping("/list")
    @Operation(summary = "获取观众列表")
    public ApiResponse getViewerList(@RequestParam Long liveRoomId) {
        ThrowUtils.throwIf(liveRoomId == null || liveRoomId <= 0, ErrorCode.PARAMS_ERROR);
        List<LiveRoomViewerVO> result = liveRoomViewerService.getViewerList(liveRoomId);
        return ApiResponse.success(result);
    }

    @GetMapping("/total")
    @Operation(summary = "获取总观众数")
    public ApiResponse getTotalViewers(@RequestParam Long liveRoomId) {
        ThrowUtils.throwIf(liveRoomId == null || liveRoomId <= 0, ErrorCode.PARAMS_ERROR);
        Integer result = liveRoomViewerService.getTotalViewers(liveRoomId);
        return ApiResponse.success(result);
    }

    @GetMapping("/avgDuration")
    @Operation(summary = "获取平均观看时长")
    public ApiResponse getAvgViewDuration(@RequestParam Long liveRoomId) {
        ThrowUtils.throwIf(liveRoomId == null || liveRoomId <= 0, ErrorCode.PARAMS_ERROR);
        Long result = liveRoomViewerService.getAvgViewDuration(liveRoomId);
        return ApiResponse.success(result);
    }
}