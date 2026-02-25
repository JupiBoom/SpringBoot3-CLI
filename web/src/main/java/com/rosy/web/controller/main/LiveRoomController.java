package com.rosy.web.controller.main;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.dto.live.*;
import com.rosy.main.domain.vo.LiveRoomStatsVO;
import com.rosy.main.domain.vo.LiveRoomVO;
import com.rosy.main.service.ILiveRoomService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 直播间管理 前端控制器
 * </p>
 *
 * @author Rosy
 * @since 2025-02-25
 */
@RestController
@RequestMapping("/live/room")
public class LiveRoomController {

    @Resource
    private ILiveRoomService liveRoomService;

    // ==================== 直播间基础管理 ====================

    /**
     * 创建直播间
     */
    @PostMapping("/add")
    @ValidateRequest
    public ApiResponse addLiveRoom(@RequestBody LiveRoomAddRequest request, HttpServletRequest httpRequest) {
        // TODO: 从登录信息中获取创建者ID，这里使用默认值1
        Long creatorId = 1L;
        Long roomId = liveRoomService.addLiveRoom(request, creatorId);
        return ApiResponse.success(roomId);
    }

    /**
     * 删除直播间
     */
    @PostMapping("/delete")
    @ValidateRequest
    public ApiResponse deleteLiveRoom(@RequestBody IdRequest idRequest) {
        boolean result = liveRoomService.deleteLiveRoom(idRequest.getId());
        return ApiResponse.success(result);
    }

    /**
     * 更新直播间
     */
    @PostMapping("/update")
    @ValidateRequest
    public ApiResponse updateLiveRoom(@RequestBody LiveRoomUpdateRequest request, HttpServletRequest httpRequest) {
        if (request.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // TODO: 从登录信息中获取更新者ID，这里使用默认值1
        Long updaterId = 1L;
        boolean result = liveRoomService.updateLiveRoom(request, updaterId);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(true);
    }

    /**
     * 根据ID获取直播间
     */
    @GetMapping("/get")
    public ApiResponse getLiveRoomById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LiveRoomVO liveRoomVO = liveRoomService.getLiveRoomById(id);
        ThrowUtils.throwIf(liveRoomVO == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(liveRoomVO);
    }

    /**
     * 分页查询直播间列表
     */
    @PostMapping("/list/page")
    @ValidateRequest
    public ApiResponse listLiveRoomByPage(@RequestBody LiveRoomQueryRequest request) {
        Page<LiveRoomVO> page = liveRoomService.listLiveRoomByPage(request);
        return ApiResponse.success(page);
    }

    /**
     * 获取直播间列表（不分页）
     */
    @PostMapping("/list")
    @ValidateRequest
    public ApiResponse listLiveRooms(@RequestBody LiveRoomQueryRequest request) {
        List<LiveRoomVO> list = liveRoomService.listLiveRooms(request);
        return ApiResponse.success(list);
    }

    // ==================== 直播间状态管理 ====================

    /**
     * 更新直播间状态
     */
    @PostMapping("/status/update")
    @ValidateRequest
    public ApiResponse updateLiveRoomStatus(@RequestBody LiveRoomStatusRequest request, HttpServletRequest httpRequest) {
        // TODO: 从登录信息中获取更新者ID，这里使用默认值1
        Long updaterId = 1L;
        boolean result = liveRoomService.updateLiveRoomStatus(request, updaterId);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(true);
    }

    /**
     * 开始直播
     */
    @PostMapping("/start")
    @ValidateRequest
    public ApiResponse startLive(@RequestBody IdRequest idRequest, HttpServletRequest httpRequest) {
        // TODO: 从登录信息中获取更新者ID，这里使用默认值1
        Long updaterId = 1L;
        boolean result = liveRoomService.startLive(idRequest.getId(), updaterId);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(true);
    }

    /**
     * 结束直播
     */
    @PostMapping("/end")
    @ValidateRequest
    public ApiResponse endLive(@RequestBody IdRequest idRequest, HttpServletRequest httpRequest) {
        // TODO: 从登录信息中获取更新者ID，这里使用默认值1
        Long updaterId = 1L;
        boolean result = liveRoomService.endLive(idRequest.getId(), updaterId);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(true);
    }

    /**
     * 切换讲解商品
     */
    @PostMapping("/switch/item")
    @ValidateRequest
    public ApiResponse switchCurrentItem(@RequestBody LiveRoomSwitchItemRequest request, HttpServletRequest httpRequest) {
        // TODO: 从登录信息中获取更新者ID，这里使用默认值1
        Long updaterId = 1L;
        boolean result = liveRoomService.switchCurrentItem(request, updaterId);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(true);
    }

    // ==================== 直播间互动 ====================

    /**
     * 增加点赞数
     */
    @PostMapping("/like")
    @ValidateRequest
    public ApiResponse incrementLikeCount(@RequestBody IdRequest idRequest, HttpServletRequest httpRequest) {
        // TODO: 从登录信息中获取用户ID，这里使用默认值1
        Long userId = 1L;
        boolean result = liveRoomService.incrementLikeCount(idRequest.getId(), 1, userId);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(true);
    }

    // ==================== 直播间数据统计 ====================

    /**
     * 获取直播间统计数据
     */
    @GetMapping("/stats")
    public ApiResponse getLiveRoomStats(long roomId) {
        if (roomId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LiveRoomStatsVO statsVO = liveRoomService.getLiveRoomStats(roomId);
        return ApiResponse.success(statsVO);
    }
}
