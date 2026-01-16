package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.LiveRoomViewer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface LiveRoomViewerMapper extends BaseMapper<LiveRoomViewer> {

    @Select("SELECT COUNT(*) FROM live_room_viewer WHERE live_room_id = #{liveRoomId}")
    Integer getTotalViewers(@Param("liveRoomId") Long liveRoomId);

    @Select("SELECT AVG(duration) FROM live_room_viewer WHERE live_room_id = #{liveRoomId}")
    Long getAvgViewDuration(@Param("liveRoomId") Long liveRoomId);

    @Select("SELECT * FROM live_room_viewer WHERE live_room_id = #{liveRoomId} ORDER BY enter_time ASC")
    List<LiveRoomViewer> getViewerList(@Param("liveRoomId") Long liveRoomId);
}