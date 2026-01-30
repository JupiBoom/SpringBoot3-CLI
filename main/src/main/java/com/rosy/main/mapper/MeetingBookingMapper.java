package com.rosy.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.main.domain.entity.MeetingBooking;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 会议预约Mapper接口
 */
@Mapper
public interface MeetingBookingMapper extends BaseMapper<MeetingBooking> {

    /**
     * 查询指定会议室在指定时间段内的预约记录
     * @param roomId 会议室ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 预约记录列表
     */
    @Select("SELECT * FROM meeting_booking WHERE room_id = #{roomId} " +
            "AND status IN (0, 1) " +
            "AND ((start_time <= #{startTime} AND end_time > #{startTime}) " +
            "OR (start_time < #{endTime} AND end_time >= #{endTime}) " +
            "OR (start_time >= #{startTime} AND end_time <= #{endTime})) " +
            "AND is_deleted = 0")
    List<MeetingBooking> findConflictingBookings(@Param("roomId") Long roomId, 
                                               @Param("startTime") LocalDateTime startTime, 
                                               @Param("endTime") LocalDateTime endTime);

    /**
     * 查询指定会议室在指定日期范围内的预约记录
     * @param roomId 会议室ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 预约记录列表
     */
    @Select("SELECT * FROM meeting_booking WHERE room_id = #{roomId} " +
            "AND DATE(start_time) >= #{startDate} AND DATE(start_time) <= #{endDate} " +
            "AND status = 1 AND is_deleted = 0")
    List<MeetingBooking> findBookingsByDateRange(@Param("roomId") Long roomId, 
                                               @Param("startDate") String startDate, 
                                               @Param("endDate") String endDate);
}