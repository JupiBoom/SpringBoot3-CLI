package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.LiveRoomItem;
import com.rosy.main.domain.vo.LiveRoomItemVO;

import java.util.List;

public interface ILiveRoomItemService extends IService<LiveRoomItem> {

    LiveRoomItemVO getLiveRoomItemVO(LiveRoomItem liveRoomItem);

    List<LiveRoomItemVO> getLiveRoomItems(Long liveRoomId);

    boolean addLiveRoomItem(Long liveRoomId, Long itemId, String sellingPoints, Integer sortOrder);

    boolean removeLiveRoomItem(Long liveRoomId, Long itemId);
}