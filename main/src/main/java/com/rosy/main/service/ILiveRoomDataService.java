package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.LiveRoomData;
import com.rosy.main.domain.vo.LiveRoomAnalyticsVO;
import com.rosy.main.domain.vo.LiveRoomDataVO;
import com.rosy.main.domain.vo.ProductRankingVO;
import com.rosy.main.domain.vo.ViewerRetentionVO;

import java.math.BigDecimal;
import java.util.List;

public interface ILiveRoomDataService extends IService<LiveRoomData> {

    LiveRoomDataVO recordLiveRoomData(LiveRoomData liveRoomData);

    List<LiveRoomDataVO> getLiveRoomDataList(Long liveRoomId);

    LiveRoomDataVO getLatestData(Long liveRoomId);

    LiveRoomAnalyticsVO getAnalytics(Long liveRoomId);

    List<ProductRankingVO> getProductRanking(Long liveRoomId);

    ViewerRetentionVO getViewerRetention(Long liveRoomId);
}