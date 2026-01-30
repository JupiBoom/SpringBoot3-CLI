package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.live.*;
import com.rosy.main.domain.entity.LiveRoom;
import com.rosy.main.domain.vo.LiveRoomConversionStatsVO;
import com.rosy.main.domain.vo.LiveRoomVO;
import com.rosy.main.domain.vo.LiveRoomWithStatsVO;
import com.rosy.main.domain.vo.LiveRetentionDataVO;

import java.util.List;

public interface ILiveRoomService extends IService<LiveRoom> {

    Long createRoom(LiveRoomCreateRequest request);

    boolean updateRoom(LiveRoomUpdateRequest request);

    boolean updateRoomStatus(LiveRoomStatusUpdateRequest request);

    LiveRoomVO getRoomDetail(Long roomId);

    LiveRoomWithStatsVO getRoomWithStats(Long roomId);

    List<LiveRoomVO> getRoomList(LiveRoomQueryRequest request);

    boolean startLive(Long roomId);

    boolean endLive(Long roomId);

    boolean switchExplainingProduct(Long roomId, Long productId);

    LiveRoomConversionStatsVO getConversionStats(Long roomId);

    List<LiveRetentionDataVO> getRetentionCurve(Long roomId);
}
