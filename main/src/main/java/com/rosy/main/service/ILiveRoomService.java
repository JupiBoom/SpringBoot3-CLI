package com.rosy.main.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.live.LiveRoomQueryRequest;
import com.rosy.main.domain.entity.LiveRoom;
import com.rosy.main.domain.vo.LiveRoomVO;

public interface ILiveRoomService extends IService<LiveRoom> {

    LiveRoomVO getLiveRoomVO(LiveRoom liveRoom);

    LambdaQueryWrapper<LiveRoom> getQueryWrapper(LiveRoomQueryRequest liveRoomQueryRequest);

    boolean startLive(Long id);

    boolean endLive(Long id);

    boolean switchItem(Long liveRoomId, Long itemId);
}