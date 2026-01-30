package com.rosy.main.service;

import com.rosy.main.domain.vo.LiveRoomConversionRateVO;
import com.rosy.main.domain.vo.LiveRoomRetentionVO;

public interface ILiveRoomAnalysisService {

    LiveRoomConversionRateVO calculateConversionRate(Long liveRoomId);

    LiveRoomRetentionVO getAudienceRetentionCurve(Long liveRoomId);
}