package com.rosy.meeting.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.meeting.domain.entity.MeetingReservation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface MeetingReservationMapper extends BaseMapper<MeetingReservation> {
    @Select("<script>"
            + "SELECT COUNT(*) FROM meeting_reservation WHERE room_id = #{roomId} AND is_deleted = 0 AND status IN (1, 4) "
            + "AND ((start_time &lt; #{endTime} AND end_time &gt; #{startTime})) "
            + "<if test='reservationId != null'> AND id != #{reservationId} </if>"
            + "</script>")
    int countConflictingReservations(@Param("roomId") Long roomId,
                                      @Param("startTime") LocalDateTime startTime,
                                      @Param("endTime") LocalDateTime endTime,
                                      @Param("reservationId") Long reservationId);
    
    @Select("SELECT * FROM meeting_reservation WHERE room_id = #{roomId} AND status IN (1, 4) "
            + "AND start_time &lt;= #{endTime} AND end_time &gt;= #{startTime}")
    List<MeetingReservation> findConflictingReservations(@Param("roomId") Long roomId,
                                                         @Param("startTime") LocalDateTime startTime,
                                                         @Param("endTime") LocalDateTime endTime);
}