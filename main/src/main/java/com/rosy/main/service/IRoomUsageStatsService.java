package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.RoomUsageStats;
import com.rosy.main.domain.vo.RoomUsageStatsVO;

import java.time.LocalDate;
import java.util.List;

/**
 * 会议室使用统计Service接口
 */
public interface IRoomUsageStatsService extends IService<RoomUsageStats> {

    /**
     * 获取指定会议室在指定日期范围内的统计数据
     * @param roomId 会议室ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 统计数据列表
     */
    List<RoomUsageStatsVO> getRoomUsageStats(Long roomId, LocalDate startDate, LocalDate endDate);

    /**
     * 获取所有会议室在指定日期范围内的统计数据
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 统计数据列表
     */
    List<RoomUsageStatsVO> getAllRoomUsageStats(LocalDate startDate, LocalDate endDate);

    /**
     * 生成指定日期的统计数据
     * @param date 统计日期
     */
    void generateDailyStats(LocalDate date);

    /**
     * 生成指定日期范围内的统计数据
     * @param startDate 开始日期
     * @param endDate 结束日期
     */
    void generateRangeStats(LocalDate startDate, LocalDate endDate);
}