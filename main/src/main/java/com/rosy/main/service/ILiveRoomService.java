package com.rosy.main.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.live.LiveProductAddRequest;
import com.rosy.main.domain.dto.live.LiveRoomAddRequest;
import com.rosy.main.domain.dto.live.LiveRoomQueryRequest;
import com.rosy.main.domain.dto.live.LiveRoomUpdateRequest;
import com.rosy.main.domain.entity.LiveProduct;
import com.rosy.main.domain.entity.LiveRoom;
import com.rosy.main.domain.vo.LiveAudienceRetentionVO;
import com.rosy.main.domain.vo.LiveProductRankVO;
import com.rosy.main.domain.vo.LiveRoomVO;
import com.rosy.main.domain.vo.LiveStatisticsVO;

import java.util.List;

public interface ILiveRoomService extends IService<LiveRoom> {

    Long createRoom(LiveRoomAddRequest request);

    Boolean updateRoom(LiveRoomUpdateRequest request);

    Boolean startLive(Long roomId);

    Boolean endLive(Long roomId);

    Boolean switchExplainingProduct(Long roomId, Long liveProductId);

    LiveRoomVO getRoomDetail(Long roomId);

    Page<LiveRoomVO> getRoomPage(LiveRoomQueryRequest request);

    LambdaQueryWrapper<LiveRoom> getQueryWrapper(LiveRoomQueryRequest request);

    LiveRoomVO objToVo(LiveRoom liveRoom);

    List<LiveRoomVO> objToVo(List<LiveRoom> liveRoomList);

    Boolean addProductToRoom(LiveProductAddRequest request);

    Boolean removeProductFromRoom(Long liveProductId);

    LiveStatisticsVO getRoomStatistics(Long roomId);

    List<LiveProductRankVO> getProductRank(Long roomId);

    List<LiveAudienceRetentionVO> getAudienceRetention(Long roomId);
}
