package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.LiveViewerStats;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 直播间观众数据统计表 Mapper 接口
 * </p>
 *
 * @author Rosy
 * @since 2025-02-25
 */
public interface LiveViewerStatsMapper extends BaseMapper<LiveViewerStats> {

    /**
     * 更新观众离开时间和停留时长
     */
    @Update("UPDATE live_viewer_stats SET leave_time = #{leaveTime}, stay_duration = TIMESTAMPDIFF(SECOND, enter_time, #{leaveTime}) " +
            "WHERE room_id = #{roomId} AND user_id = #{userId} AND leave_time IS NULL")
    int updateLeaveTime(@Param("roomId") Long roomId, @Param("userId") Long userId, @Param("leaveTime") LocalDateTime leaveTime);

    /**
     * 查询直播间累计观看人数
     */
    @Select("SELECT COUNT(DISTINCT user_id) FROM live_viewer_stats WHERE room_id = #{roomId}")
    Long countUniqueViewers(@Param("roomId") Long roomId);

    /**
     * 查询直播间平均停留时长（秒）
     */
    @Select("SELECT COALESCE(AVG(stay_duration), 0) FROM live_viewer_stats WHERE room_id = #{roomId} AND stay_duration > 0")
    Double avgStayDuration(@Param("roomId") Long roomId);

    /**
     * 查询直播间每分钟观众留存数据
     */
    @Select("SELECT DATE_FORMAT(enter_time, '%Y-%m-%d %H:%i') as time_slot, COUNT(*) as enter_count " +
            "FROM live_viewer_stats WHERE room_id = #{roomId} AND enter_time BETWEEN #{startTime} AND #{endTime} " +
            "GROUP BY time_slot ORDER BY time_slot")
    List<Map<String, Object>> selectMinuteEnterStats(@Param("roomId") Long roomId, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 查询直播间每分钟观众离开数据
     */
    @Select("SELECT DATE_FORMAT(leave_time, '%Y-%m-%d %H:%i') as time_slot, COUNT(*) as leave_count " +
            "FROM live_viewer_stats WHERE room_id = #{roomId} AND leave_time BETWEEN #{startTime} AND #{endTime} " +
            "GROUP BY time_slot ORDER BY time_slot")
    List<Map<String, Object>> selectMinuteLeaveStats(@Param("roomId") Long roomId, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 查询直播间观众停留时长分布
     */
    @Select("SELECT " +
            "SUM(CASE WHEN stay_duration < 60 THEN 1 ELSE 0 END) as less_than_1min, " +
            "SUM(CASE WHEN stay_duration >= 60 AND stay_duration < 300 THEN 1 ELSE 0 END) as between_1_5min, " +
            "SUM(CASE WHEN stay_duration >= 300 AND stay_duration < 600 THEN 1 ELSE 0 END) as between_5_10min, " +
            "SUM(CASE WHEN stay_duration >= 600 AND stay_duration < 1800 THEN 1 ELSE 0 END) as between_10_30min, " +
            "SUM(CASE WHEN stay_duration >= 1800 THEN 1 ELSE 0 END) as more_than_30min " +
            "FROM live_viewer_stats WHERE room_id = #{roomId} AND stay_duration > 0")
    Map<String, Object> selectStayDurationDistribution(@Param("roomId") Long roomId);
}
