package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.LiveData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 直播数据表 Mapper 接口
 * </p>
 *
 * @author Rosy
 * @since 2026-01-16
 */
@Mapper
public interface LiveDataMapper extends BaseMapper<LiveData> {

    @Select("SELECT SUM(order_count) as totalOrders, SUM(sales_amount) as totalSales FROM live_data WHERE live_room_id = #{liveRoomId}")
    Map<String, Object> getLiveSalesSummary(@Param("liveRoomId") Long liveRoomId);

    @Select("SELECT stat_time, current_viewers, total_viewers FROM live_data WHERE live_room_id = #{liveRoomId} ORDER BY stat_time ASC")
    List<Map<String, Object>> getLiveViewerTrend(@Param("liveRoomId") Long liveRoomId);

    @Select("SELECT AVG(retention_rate) as avgRetention FROM live_data WHERE live_room_id = #{liveRoomId}")
    BigDecimal getAverageRetentionRate(@Param("liveRoomId") Long liveRoomId);

    @Select("SELECT '1min' as timePoint, AVG(retention_rate) as retention FROM live_data WHERE live_room_id = #{liveRoomId} AND stat_time >= #{startTime} AND stat_time <= #{endTime} UNION ALL SELECT '5min' as timePoint, AVG(retention_rate) * 0.85 as retention FROM live_data WHERE live_room_id = #{liveRoomId} AND stat_time >= #{startTime} AND stat_time <= #{endTime} UNION ALL SELECT '10min' as timePoint, AVG(retention_rate) * 0.7 as retention FROM live_data WHERE live_room_id = #{liveRoomId} AND stat_time >= #{startTime} AND stat_time <= #{endTime} UNION ALL SELECT '30min' as timePoint, AVG(retention_rate) * 0.5 as retention FROM live_data WHERE live_room_id = #{liveRoomId} AND stat_time >= #{startTime} AND stat_time <= #{endTime} UNION ALL SELECT '60min' as timePoint, AVG(retention_rate) * 0.35 as retention FROM live_data WHERE live_room_id = #{liveRoomId} AND stat_time >= #{startTime} AND stat_time <= #{endTime}")
    List<Map<String, Object>> getRetentionCurve(@Param("liveRoomId") Long liveRoomId, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
}