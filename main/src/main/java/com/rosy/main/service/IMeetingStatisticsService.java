package com.rosy.main.service;

import com.rosy.main.domain.dto.MeetingStatisticsRequest;
import com.rosy.main.domain.vo.MeetingStatisticsVO;

public interface IMeetingStatisticsService {

    MeetingStatisticsVO getStatistics(MeetingStatisticsRequest request);
}
