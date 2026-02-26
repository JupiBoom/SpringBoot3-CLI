package com.rosy.web.controller.main;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.PageUtils;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.dto.liveroom.*;
import com.rosy.main.domain.entity.LiveRoom;
import com.rosy.main.domain.vo.LiveRoomVO;
import com.rosy.main.service.ILiveRoomService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/liveRoom")
public class LiveRoomController {

    @Resource
    private ILiveRoomService liveRoomService;

    @PostMapping("/add")
    @ValidateRequest
    public ApiResponse addLiveRoom(@RequestBody LiveRoomAddRequest addRequest) {
        LiveRoom liveRoom = BeanUtil.copyProperties(addRequest, LiveRoom.class);
        liveRoom.setStatus((byte) 0);
        liveRoom.setViewerCount(0);
        liveRoom.setTotalViewerCount(0);
        liveRoom.setTotalOrders(0);
        liveRoom.setTotalSales(BigDecimal.ZERO);

        boolean result = liveRoomService.save(liveRoom);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(liveRoom.getId());
    }

    @PostMapping("/delete")
    @ValidateRequest
    public ApiResponse deleteLiveRoom(@RequestBody IdRequest idRequest) {
        boolean result = liveRoomService.removeById(idRequest.getId());
        return ApiResponse.success(result);
    }

    @PostMapping("/update")
    @ValidateRequest
    public ApiResponse updateLiveRoom(@RequestBody LiveRoomUpdateRequest updateRequest) {
        if (updateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LiveRoom liveRoom = BeanUtil.copyProperties(updateRequest, LiveRoom.class);
        boolean result = liveRoomService.updateById(liveRoom);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(true);
    }

    @GetMapping("/get")
    public ApiResponse getLiveRoomById(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LiveRoom liveRoom = liveRoomService.getById(id);
        ThrowUtils.throwIf(liveRoom == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(liveRoom);
    }

    @GetMapping("/get/vo")
    public ApiResponse getLiveRoomVOById(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LiveRoom liveRoom = liveRoomService.getById(id);
        ThrowUtils.throwIf(liveRoom == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(liveRoomService.getLiveRoomVO(liveRoom));
    }

    @PostMapping("/list/page")
    @ValidateRequest
    public ApiResponse listLiveRoomByPage(@RequestBody LiveRoomQueryRequest queryRequest) {
        long current = queryRequest.getCurrent();
        long size = queryRequest.getPageSize();
        Page<LiveRoom> page = liveRoomService.page(
                new Page<>(current, size),
                liveRoomService.getQueryWrapper(queryRequest)
        );
        return ApiResponse.success(page);
    }

    @PostMapping("/list/page/vo")
    @ValidateRequest
    public ApiResponse listLiveRoomVOByPage(@RequestBody LiveRoomQueryRequest queryRequest) {
        long current = queryRequest.getCurrent();
        long size = queryRequest.getPageSize();
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<LiveRoom> page = liveRoomService.page(
                new Page<>(current, size),
                liveRoomService.getQueryWrapper(queryRequest)
        );
        Page<LiveRoomVO> voPage = PageUtils.convert(page, liveRoomService::getLiveRoomVO);
        return ApiResponse.success(voPage);
    }

    @PostMapping("/status/update")
    @ValidateRequest
    public ApiResponse updateStatus(@RequestBody LiveRoomStatusRequest statusRequest) {
        boolean result = liveRoomService.updateLiveRoomStatus(
                statusRequest.getLiveRoomId(),
                statusRequest.getStatus()
        );
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(true);
    }

    @PostMapping("/start")
    @ValidateRequest
    public ApiResponse startLive(@RequestBody IdRequest idRequest) {
        boolean result = liveRoomService.startLive(idRequest.getId());
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(true);
    }

    @PostMapping("/end")
    @ValidateRequest
    public ApiResponse endLive(@RequestBody IdRequest idRequest) {
        boolean result = liveRoomService.endLive(idRequest.getId());
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(true);
    }
}
