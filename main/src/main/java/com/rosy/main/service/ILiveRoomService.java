package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.LiveRoom;
import com.rosy.main.domain.vo.LiveRoomVO;

import java.util.List;

public interface ILiveRoomService extends IService<LiveRoom> {

    LiveRoomVO createLiveRoom(LiveRoom liveRoom);

    LiveRoomVO updateLiveRoom(LiveRoom liveRoom);

    boolean deleteLiveRoom(Long id);

    LiveRoomVO getLiveRoomDetail(Long id);

    List<LiveRoomVO> getLiveRoomList(Long anchorId, Byte status);

    boolean startLive(Long id);

    boolean endLive(Long id);

    LiveRoomVO getLiveRoomVO(LiveRoom liveRoom);
}