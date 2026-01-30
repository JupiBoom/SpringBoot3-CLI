package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.LiveRoomItemRank;

import java.util.List;

public interface ILiveRoomItemRankService extends IService<LiveRoomItemRank> {

    List<LiveRoomItemRank> getLiveRoomItemRank(Long liveRoomId);

    boolean updateItemRank(Long liveRoomId);
}
