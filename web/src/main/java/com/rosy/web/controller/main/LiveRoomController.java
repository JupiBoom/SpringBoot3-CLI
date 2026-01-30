package com.rosy.web.controller.main;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.utils.PageUtils;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.dto.live.LiveRoomAddRequest;
import com.rosy.main.domain.dto.live.LiveRoomQueryRequest;
import com.rosy.main.domain.dto.live.LiveRoomUpdateRequest;
import com.rosy.main.domain.entity.LiveRoom;
import com.rosy.main.domain.vo.LiveRoomVO;
import com.rosy.main.service.ILiveRoomService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/live/room")
public class LiveRoomController {

    @Resource
    ILiveRoomService liveRoomService;

    // region 增删改查

    @PostMapping("/add")
    @ValidateRequest
    public ApiResponse addLiveRoom(@RequestBody LiveRoomAddRequest liveRoomAddRequest) {
        LiveRoom liveRoom = new LiveRoom();
        BeanUtils.copyProperties(liveRoomAddRequest, liveRoom);
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
    public ApiResponse updateLiveRoom(@RequestBody LiveRoomUpdateRequest liveRoomUpdateRequest) {
        if (liveRoomUpdateRequest.getId() == null) {
            return ApiResponse.error(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMessage());
        }
        LiveRoom liveRoom = BeanUtil.copyProperties(liveRoomUpdateRequest, LiveRoom.class);
        boolean result = liveRoomService.updateById(liveRoom);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(true);
    }

    @GetMapping("/get")
    public ApiResponse getLiveRoomById(long id) {
        if (id <= 0) {
            return ApiResponse.error(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMessage());
        }
        LiveRoom liveRoom = liveRoomService.getById(id);
        ThrowUtils.throwIf(liveRoom == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(liveRoom);
    }

    @GetMapping("/get/vo")
    public ApiResponse getLiveRoomVOById(long id) {
        if (id <= 0) {
            return ApiResponse.error(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMessage());
        }
        LiveRoom liveRoom = liveRoomService.getById(id);
        ThrowUtils.throwIf(liveRoom == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(liveRoomService.getLiveRoomVO(liveRoom));
    }

    @PostMapping("/list/page")
    @ValidateRequest
    public ApiResponse listLiveRoomByPage(@RequestBody LiveRoomQueryRequest liveRoomQueryRequest) {
        long current = liveRoomQueryRequest.getCurrent();
        long size = liveRoomQueryRequest.getPageSize();
        Page<LiveRoom> liveRoomPage = liveRoomService.page(new Page<>(current, size), liveRoomService.getQueryWrapper(liveRoomQueryRequest));
        return ApiResponse.success(liveRoomPage);
    }

    @PostMapping("/list/page/vo")
    @ValidateRequest
    public ApiResponse listLiveRoomVOByPage(@RequestBody LiveRoomQueryRequest liveRoomQueryRequest) {
        long current = liveRoomQueryRequest.getCurrent();
        long size = liveRoomQueryRequest.getPageSize();
        Page<LiveRoom> liveRoomPage = liveRoomService.page(new Page<>(current, size), liveRoomService.getQueryWrapper(liveRoomQueryRequest));
        Page<LiveRoomVO> liveRoomVOPage = PageUtils.convert(liveRoomPage, liveRoomService::getLiveRoomVO);
        return ApiResponse.success(liveRoomVOPage);
    }

    // endregion

    // 直播状态管理
    @PostMapping("/start/{id}")
    public ApiResponse startLive(@PathVariable Long id) {
        boolean result = liveRoomService.startLive(id);
        return result ? ApiResponse.success() : ApiResponse.error(ErrorCode.OPERATION_ERROR.getCode(), ErrorCode.OPERATION_ERROR.getMessage());
    }

    @PostMapping("/end/{id}")
    public ApiResponse endLive(@PathVariable Long id) {
        boolean result = liveRoomService.endLive(id);
        return result ? ApiResponse.success() : ApiResponse.error(ErrorCode.OPERATION_ERROR.getCode(), ErrorCode.OPERATION_ERROR.getMessage());
    }

    @PostMapping("/switch-item")
    public ApiResponse switchItem(@RequestParam Long liveRoomId, @RequestParam Long itemId) {
        boolean result = liveRoomService.switchItem(liveRoomId, itemId);
        return result ? ApiResponse.success() : ApiResponse.error(ErrorCode.OPERATION_ERROR.getCode(), ErrorCode.OPERATION_ERROR.getMessage());
    }
}