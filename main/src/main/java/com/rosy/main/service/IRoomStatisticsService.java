package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.RoomStatistics;
import com.rosy.main.domain.vo.RoomStatisticsVO;

import java.time.LocalDate;
import java.util.List;

public interface IRoomStatisticsService extends IService<RoomStatistics> {

    RoomStatisticsVO getRoomStatistics(Long roomId, LocalDate date);

    List<RoomStatisticsVO> getRoomStatisticsRange(Long roomId, LocalDate startDate, LocalDate endDate);

    void generateDailyStatistics(LocalDate date);
}
