package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.LiveRoomViewer;
import com.rosy.main.domain.vo.LiveRoomViewerVO;

import java.util.List;

public interface ILiveRoomViewerService extends IService<LiveRoomViewer> {

    LiveRoomViewerVO recordViewerEntry(LiveRoomViewer liveRoomViewer);

    boolean recordViewerExit(Long id);

    List<LiveRoomViewerVO> getViewerList(Long liveRoomId);

    Integer getTotalViewers(Long liveRoomId);

    Long getAvgViewDuration(Long liveRoomId);

    LiveRoomViewerVO getLiveRoomViewerVO(LiveRoomViewer liveRoomViewer);
}