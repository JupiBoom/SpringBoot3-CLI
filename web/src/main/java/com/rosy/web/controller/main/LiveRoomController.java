package com.rosy.web.controller.main;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rosy.common.domain.entity.ApiResponse;
import com.rosy.main.domain.dto.live.LiveRoomAddRequest;
import com.rosy.main.domain.dto.live.LiveRoomQueryRequest;
import com.rosy.main.domain.dto.live.LiveRoomStatusUpdateRequest;
import com.rosy.main.domain.dto.live.LiveRoomUpdateRequest;
import com.rosy.main.domain.vo.LiveRoomVO;
import com.rosy.main.domain.vo.LiveStatsVO;
import com.rosy.main.service.ILiveRoomService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 直播间管理控制器
 * </p>
 *
 * @author Rosy
 * @since 2026-01-16
 */
@RestController
@RequestMapping("/api/live/room")
public class LiveRoomController {

    @Resource
    ILiveRoomService liveRoomService;

    @PostMapping("/create")
    public ApiResponse createLiveRoom(@RequestBody LiveRoomAddRequest request) {
        LiveRoomVO liveRoomVO = liveRoomService.createLiveRoom(request);
        return ApiResponse.success(liveRoomVO);
    }

    @PutMapping("/update")
    public ApiResponse updateLiveRoom(@RequestBody LiveRoomUpdateRequest request) {
        LiveRoomVO liveRoomVO = liveRoomService.updateLiveRoom(request);
        return ApiResponse.success(liveRoomVO);
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse deleteLiveRoom(@PathVariable Long id) {
        boolean result = liveRoomService.deleteLiveRoom(id);
        return ApiResponse.success(result);
    }

    @GetMapping("/detail/{id}")
    public ApiResponse getLiveRoomDetail(@PathVariable Long id) {
        LiveRoomVO liveRoomVO = liveRoomService.getLiveRoomDetail(id);
        return ApiResponse.success(liveRoomVO);
    }

    @PostMapping("/list")
    public ApiResponse listLiveRooms(@RequestBody LiveRoomQueryRequest request,
                                     @RequestParam(defaultValue = "1") Integer pageNum,
                                     @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<LiveRoomVO> page = liveRoomService.listLiveRooms(request, pageNum, pageSize);
        return ApiResponse.success(page);
    }

    @PostMapping("/status/update")
    public ApiResponse updateLiveRoomStatus(@RequestBody LiveRoomStatusUpdateRequest request) {
        LiveRoomVO liveRoomVO = liveRoomService.updateLiveRoomStatus(request);
        return ApiResponse.success(liveRoomVO);
    }

    @PostMapping("/product/switch")
    public ApiResponse switchCurrentProduct(@RequestParam Long liveRoomId, @RequestParam Long productId) {
        boolean result = liveRoomService.switchCurrentProduct(liveRoomId, productId);
        return ApiResponse.success(result);
    }

    @GetMapping("/stats/{id}")
    public ApiResponse getLiveRoomStats(@PathVariable Long id) {
        LiveStatsVO stats = liveRoomService.getLiveRoomStats(id);
        return ApiResponse.success(stats);
    }
}
