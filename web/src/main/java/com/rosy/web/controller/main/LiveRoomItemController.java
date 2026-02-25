package com.rosy.web.controller.main;

import com.rosy.common.annotation.ValidateRequest;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.common.utils.ThrowUtils;
import com.rosy.main.domain.dto.live.LiveRoomItemAddRequest;
import com.rosy.main.domain.dto.live.LiveRoomItemUpdateRequest;
import com.rosy.main.domain.vo.LiveRoomItemVO;
import com.rosy.main.service.ILiveRoomItemService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 直播间商品管理 前端控制器
 * </p>
 *
 * @author Rosy
 * @since 2025-02-25
 */
@RestController
@RequestMapping("/live/room/item")
public class LiveRoomItemController {

    @Resource
    private ILiveRoomItemService liveRoomItemService;

    /**
     * 添加商品到直播间
     */
    @PostMapping("/add")
    @ValidateRequest
    public ApiResponse addLiveRoomItem(@RequestBody LiveRoomItemAddRequest request, HttpServletRequest httpRequest) {
        // TODO: 从登录信息中获取创建者ID，这里使用默认值1
        Long creatorId = 1L;
        Long id = liveRoomItemService.addLiveRoomItem(request, creatorId);
        return ApiResponse.success(id);
    }

    /**
     * 删除直播间商品
     */
    @PostMapping("/delete")
    @ValidateRequest
    public ApiResponse deleteLiveRoomItem(@RequestBody IdRequest idRequest) {
        boolean result = liveRoomItemService.deleteLiveRoomItem(idRequest.getId());
        return ApiResponse.success(result);
    }

    /**
     * 更新直播间商品
     */
    @PostMapping("/update")
    @ValidateRequest
    public ApiResponse updateLiveRoomItem(@RequestBody LiveRoomItemUpdateRequest request, HttpServletRequest httpRequest) {
        if (request.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // TODO: 从登录信息中获取更新者ID，这里使用默认值1
        Long updaterId = 1L;
        boolean result = liveRoomItemService.updateLiveRoomItem(request, updaterId);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ApiResponse.success(true);
    }

    /**
     * 根据ID获取直播间商品
     */
    @GetMapping("/get")
    public ApiResponse getLiveRoomItemById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LiveRoomItemVO itemVO = liveRoomItemService.getLiveRoomItemById(id);
        ThrowUtils.throwIf(itemVO == null, ErrorCode.NOT_FOUND_ERROR);
        return ApiResponse.success(itemVO);
    }

    /**
     * 获取直播间的商品列表
     */
    @GetMapping("/list")
    public ApiResponse listItemsByRoomId(long roomId) {
        if (roomId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<LiveRoomItemVO> list = liveRoomItemService.listItemsByRoomId(roomId);
        return ApiResponse.success(list);
    }

    /**
     * 获取直播间商品销售排行榜
     */
    @GetMapping("/top")
    public ApiResponse getTopSellingItems(long roomId, Integer limit) {
        if (roomId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (limit == null || limit <= 0) {
            limit = 10;
        }
        List<LiveRoomItemVO> list = liveRoomItemService.getTopSellingItems(roomId, limit);
        return ApiResponse.success(list);
    }
}
