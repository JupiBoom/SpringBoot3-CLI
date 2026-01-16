package com.rosy.main.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.live.LiveRoomAddRequest;
import com.rosy.main.domain.dto.live.LiveRoomQueryRequest;
import com.rosy.main.domain.dto.live.LiveRoomStatusUpdateRequest;
import com.rosy.main.domain.dto.live.LiveRoomUpdateRequest;
import com.rosy.main.domain.entity.LiveRoom;
import com.rosy.main.domain.vo.LiveRoomVO;
import com.rosy.main.domain.vo.LiveStatsVO;

/**
 * <p>
 * 直播间表 服务类
 * </p>
 *
 * @author Rosy
 * @since 2026-01-16
 */
public interface ILiveRoomService extends IService<LiveRoom> {

    LiveRoomVO getLiveRoomVO(LiveRoom liveRoom);

    LambdaQueryWrapper<LiveRoom> getQueryWrapper(LiveRoomQueryRequest liveRoomQueryRequest);

    LiveRoomVO createLiveRoom(LiveRoomAddRequest request);

    LiveRoomVO updateLiveRoom(LiveRoomUpdateRequest request);

    boolean deleteLiveRoom(Long liveRoomId);

    LiveRoomVO getLiveRoomDetail(Long liveRoomId);

    Page<LiveRoomVO> listLiveRooms(LiveRoomQueryRequest request, int pageNum, int pageSize);

    LiveRoomVO updateLiveRoomStatus(LiveRoomStatusUpdateRequest request);

    LiveStatsVO getLiveRoomStats(Long liveRoomId);

    boolean switchCurrentProduct(Long liveRoomId, Long productId);
}