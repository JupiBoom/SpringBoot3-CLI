package com.rosy.web.controller.main;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.dto.liveRoom.LiveRoomAddRequest;
import com.rosy.main.domain.dto.liveRoom.LiveRoomQueryRequest;
import com.rosy.main.domain.dto.liveRoom.LiveRoomUpdateRequest;
import com.rosy.main.domain.entity.LiveRoom;
import com.rosy.main.domain.vo.LiveRoomVO;
import com.rosy.main.service.ILiveRoomService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 直播间表 前端控制器
 * </p>
 *
 * @author Rosy
 * @since 2025-01-30
 */
@RestController
@RequestMapping("/live-room")
public class LiveRoomController {
    @Resource
    ILiveRoomService liveRoomService;

    // region 增删改查

    /**
     * 创建直播间
     */
    @PostMapping("/add")
    @ValidateRequest
    public ApiResponse addLiveRoom(@RequestBody LiveRoomAddRequest liveRoomAddRequest) {
        Long liveRoomId = liveRoomService.createLiveRoom(liveRoomAddRequest);
        ThrowUtils.throwIf(liveRoomId == null, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(liveRoomId);
    }

    /**
     * 删除直播间
     */
    @PostMapping("/delete")
    @ValidateRequest
    public ApiResponse deleteLiveRoom(@RequestBody IdRequest idRequest) {
        boolean result = liveRoomService.removeById(idRequest.getId());
        return ApiResponse.success(result);
    }

    /**
     * 更新直播间
     */
    @PostMapping("/update")
    @ValidateRequest
    public ApiResponse updateLiveRoom(@RequestBody LiveRoomUpdateRequest liveRoomUpdateRequest) {
        if (liveRoomUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = liveRoomService.updateLiveRoom(liveRoomUpdateRequest);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(true);
    }

    /**
     * 根据 id 获取
     */
    @GetMapping("/get")
    public ApiResponse getLiveRoomById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LiveRoom liveRoom = liveRoomService.getById(id);
        ThrowUtils.throwIf(liveRoom == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(liveRoom);
    }

    /**
     * 根据 id 获取包装类
     */
    @GetMapping("/get/vo")
    public ApiResponse getLiveRoomVOById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        ApiResponse response = getLiveRoomById(id);
        LiveRoom liveRoom = (LiveRoom) response.getData();
        return ApiResponse.success(liveRoomService.getLiveRoomVO(liveRoom));
    }

    /**
     * 分页获取列表
     */
    @PostMapping("/list/page")
    @ValidateRequest
    public ApiResponse listLiveRoomByPage(@RequestBody LiveRoomQueryRequest liveRoomQueryRequest) {
        long current = liveRoomQueryRequest.getCurrent();
        long size = liveRoomQueryRequest.getPageSize();
        Page<LiveRoom> liveRoomPage = liveRoomService.page(new Page<>(current, size), 
                liveRoomService.getQueryWrapper(liveRoomQueryRequest));
        return ApiResponse.success(liveRoomPage);
    }

    /**
     * 分页获取封装列表
     */
    @PostMapping("/list/page/vo")
    @ValidateRequest
    public ApiResponse listLiveRoomVOByPage(@RequestBody LiveRoomQueryRequest liveRoomQueryRequest) {
        long current = liveRoomQueryRequest.getCurrent();
        long size = liveRoomQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<LiveRoomVO> liveRoomVOPage = liveRoomService.listLiveRoomVOByPage(liveRoomQueryRequest);
        return ApiResponse.success(liveRoomVOPage);
    }

    // endregion

    // region 直播控制

    /**
     * 开始直播
     */
    @PostMapping("/start")
    @ValidateRequest
    public ApiResponse startLive(@RequestBody IdRequest idRequest) {
        if (idRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = liveRoomService.startLive(idRequest.getId());
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(true);
    }

    /**
     * 结束直播
     */
    @PostMapping("/end")
    @ValidateRequest
    public ApiResponse endLive(@RequestBody IdRequest idRequest) {
        if (idRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = liveRoomService.endLive(idRequest.getId());
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(true);
    }

    /**
     * 更新观众人数
     */
    @PostMapping("/update-viewer-count")
    @ValidateRequest
    public ApiResponse updateViewerCount(@RequestBody UpdateViewerCountRequest request) {
        if (request.getLiveRoomId() == null || request.getViewerCount() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = liveRoomService.updateViewerCount(request.getLiveRoomId(), request.getViewerCount());
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(true);
    }

    // endregion

    /**
     * 更新观众人数请求
     */
    public static class UpdateViewerCountRequest {
        private Long liveRoomId;
        private Integer viewerCount;

        public Long getLiveRoomId() {
            return liveRoomId;
        }

        public void setLiveRoomId(Long liveRoomId) {
            this.liveRoomId = liveRoomId;
        }

        public Integer getViewerCount() {
            return viewerCount;
        }

        public void setViewerCount(Integer viewerCount) {
            this.viewerCount = viewerCount;
        }
    }
}