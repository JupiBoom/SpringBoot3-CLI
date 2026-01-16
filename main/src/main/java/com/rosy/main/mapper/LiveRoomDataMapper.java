package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.LiveRoomData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface LiveRoomDataMapper extends BaseMapper<LiveRoomData> {

    @Select("SELECT * FROM live_room_data WHERE live_room_id = #{liveRoomId} ORDER BY record_time DESC LIMIT 1")
    LiveRoomData getLatestData(@Param("liveRoomId") Long liveRoomId);

    @Select("SELECT SUM(total_orders) as total_orders, SUM(total_sales) as total_sales FROM live_room_data WHERE live_room_id = #{liveRoomId}")
    LiveRoomData getTotalData(@Param("liveRoomId") Long liveRoomId);

    @Select("SELECT record_time, viewer_count FROM live_room_data WHERE live_room_id = #{liveRoomId} ORDER BY record_time ASC")
    List<LiveRoomData> getViewerTrend(@Param("liveRoomId") Long liveRoomId);
}