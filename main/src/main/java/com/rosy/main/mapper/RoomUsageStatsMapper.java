package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.RoomUsageStats;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;

/**
 * 会议室使用统计Mapper接口
 */
@Mapper
public interface RoomUsageStatsMapper extends BaseMapper<RoomUsageStats> {

    /**
     * 查询指定会议室在指定日期范围内的统计数据
     * @param roomId 会议室ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 统计数据列表
     */
    @Select("SELECT * FROM room_usage_stats WHERE room_id = #{roomId} " +
            "AND stats_date >= #{startDate} AND stats_date <= #{endDate}")
    List<RoomUsageStats> findByRoomIdAndDateRange(@Param("roomId") Long roomId, 
                                                 @Param("startDate") LocalDate startDate, 
                                                 @Param("endDate") LocalDate endDate);

    /**
     * 查询指定日期范围内所有会议室的统计数据
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 统计数据列表
     */
    @Select("SELECT * FROM room_usage_stats " +
            "WHERE stats_date >= #{startDate} AND stats_date <= #{endDate}")
    List<RoomUsageStats> findByDateRange(@Param("startDate") LocalDate startDate, 
                                        @Param("endDate") LocalDate endDate);
}