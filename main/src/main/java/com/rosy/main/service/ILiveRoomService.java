package com.rosy.main.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.liveroom.LiveRoomQueryRequest;
import com.rosy.main.domain.entity.LiveRoom;
import com.rosy.main.domain.vo.LiveRoomVO;

public interface ILiveRoomService extends IService<LiveRoom> {

    LiveRoomVO getLiveRoomVO(LiveRoom liveRoom);

    LambdaQueryWrapper<LiveRoom> getQueryWrapper(LiveRoomQueryRequest queryRequest);

    boolean updateLiveRoomStatus(Long liveRoomId, Byte status);

    boolean startLive(Long liveRoomId);

    boolean endLive(Long liveRoomId);
}
