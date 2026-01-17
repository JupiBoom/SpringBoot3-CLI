package com.rosy.main.service;

import com.rosy.main.domain.vo.repair.StatisticsVO;

import java.time.LocalDateTime;

public interface IStatisticsService {

    StatisticsVO getStatistics(LocalDateTime startTime, LocalDateTime endTime);
}
