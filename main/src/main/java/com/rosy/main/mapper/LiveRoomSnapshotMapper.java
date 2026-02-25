package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.LiveRoomSnapshot;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 直播间数据快照表 Mapper 接口
 * </p>
 *
 * @author Rosy
 * @since 2025-02-25
 */
public interface LiveRoomSnapshotMapper extends BaseMapper<LiveRoomSnapshot> {

    /**
     * 查询直播间在指定时间范围内的快照数据
     */
    @Select("SELECT * FROM live_room_snapshot WHERE room_id = #{roomId} AND snapshot_time BETWEEN #{startTime} AND #{endTime} ORDER BY snapshot_time")
    List<LiveRoomSnapshot> selectByRoomIdAndTimeRange(@Param("roomId") Long roomId, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 查询直播间最新的快照数据
     */
    @Select("SELECT * FROM live_room_snapshot WHERE room_id = #{roomId} ORDER BY snapshot_time DESC LIMIT 1")
    LiveRoomSnapshot selectLatestByRoomId(@Param("roomId") Long roomId);

    /**
     * 查询直播间峰值观众数
     */
    @Select("SELECT MAX(viewer_count) FROM live_room_snapshot WHERE room_id = #{roomId}")
    Integer selectPeakViewerCount(@Param("roomId") Long roomId);
}
