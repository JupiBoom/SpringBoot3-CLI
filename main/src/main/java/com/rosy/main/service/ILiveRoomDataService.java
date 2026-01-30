package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.LiveRoomData;

public interface ILiveRoomDataService extends IService<LiveRoomData> {

    LiveRoomData getLiveRoomData(Long liveRoomId);

    boolean updateAudienceCount(Long liveRoomId, Integer audienceCount);

    boolean updateSalesData(Long liveRoomId, Integer orderCount, Double salesAmount);
}