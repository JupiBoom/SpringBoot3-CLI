package com.rosy.web.controller.main;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.dto.live.LiveProductAddRequest;
import com.rosy.main.domain.dto.live.LiveRoomAddRequest;
import com.rosy.main.domain.dto.live.LiveRoomQueryRequest;
import com.rosy.main.domain.dto.live.LiveRoomUpdateRequest;
import com.rosy.main.domain.vo.LiveAudienceRetentionVO;
import com.rosy.main.domain.vo.LiveProductRankVO;
import com.rosy.main.domain.vo.LiveRoomVO;
import com.rosy.main.domain.vo.LiveStatisticsVO;
import com.rosy.main.service.ILiveRoomService;
import jakarta.annotation.Resource;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/live/room")
public class LiveRoomController {

    @Resource
    private ILiveRoomService liveRoomService;

    @PostMapping("/create")
    @ValidateRequest
    public ApiResponse createRoom(@RequestBody LiveRoomAddRequest request) {
        Long roomId = liveRoomService.createRoom(request);
        return ApiResponse.success(roomId);
    }

    @PostMapping("/update")
    @ValidateRequest
    public ApiResponse updateRoom(@RequestBody LiveRoomUpdateRequest request) {
        if (request.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Boolean result = liveRoomService.updateRoom(request);
        return ApiResponse.success(result);
    }

    @PostMapping("/delete")
    @ValidateRequest
    public ApiResponse deleteRoom(@RequestBody IdRequest idRequest) {
        boolean result = liveRoomService.removeById(idRequest.getId());
        return ApiResponse.success(result);
    }

    @PostMapping("/start")
    @ValidateRequest
    public ApiResponse startLive(@RequestBody IdRequest idRequest) {
        Boolean result = liveRoomService.startLive(idRequest.getId());
        return ApiResponse.success(result);
    }

    @PostMapping("/end")
    @ValidateRequest
    public ApiResponse endLive(@RequestBody IdRequest idRequest) {
        Boolean result = liveRoomService.endLive(idRequest.getId());
        return ApiResponse.success(result);
    }

    @GetMapping("/get")
    public ApiResponse getRoomDetail(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LiveRoomVO roomVO = liveRoomService.getRoomDetail(id);
        return ApiResponse.success(roomVO);
    }

    @PostMapping("/list/page")
    @ValidateRequest
    public ApiResponse getRoomPage(@RequestBody LiveRoomQueryRequest request) {
        Page<LiveRoomVO> page = liveRoomService.getRoomPage(request);
        return ApiResponse.success(page);
    }

    @PostMapping("/product/add")
    @ValidateRequest
    public ApiResponse addProductToRoom(@RequestBody LiveProductAddRequest request) {
        Boolean result = liveRoomService.addProductToRoom(request);
        return ApiResponse.success(result);
    }

    @Data
    public static class SwitchProductRequest {
        private Long roomId;
        private Long liveProductId;
    }

    @PostMapping("/product/switch")
    @ValidateRequest
    public ApiResponse switchExplainingProduct(@RequestBody SwitchProductRequest request) {
        if (request.roomId == null || request.liveProductId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Boolean result = liveRoomService.switchExplainingProduct(request.roomId, request.liveProductId);
        return ApiResponse.success(result);
    }

    @GetMapping("/statistics")
    public ApiResponse getRoomStatistics(Long roomId) {
        if (roomId == null || roomId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LiveStatisticsVO statistics = liveRoomService.getRoomStatistics(roomId);
        return ApiResponse.success(statistics);
    }

    @GetMapping("/product/rank")
    public ApiResponse getProductRank(Long roomId) {
        if (roomId == null || roomId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<LiveProductRankVO> rankList = liveRoomService.getProductRank(roomId);
        return ApiResponse.success(rankList);
    }

    @GetMapping("/retention")
    public ApiResponse getAudienceRetention(Long roomId) {
        if (roomId == null || roomId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<LiveAudienceRetentionVO> retentionList = liveRoomService.getAudienceRetention(roomId);
        return ApiResponse.success(retentionList);
    }
}