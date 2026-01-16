package com.rosy.meeting.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rosy.meeting.domain.entity.MeetingBooking;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 会议预约Mapper接口
 */
@Mapper
public interface MeetingBookingMapper extends BaseMapper<MeetingBooking> {

    /**
     * 查询会议室在指定时间段内的预约数量
     * @param meetingRoomId 会议室ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param excludeId 排除的预约ID（用于更新时检查）
     * @return 预约数量
     */
    Integer countOverlappingBookings(
            @Param("meetingRoomId") Long meetingRoomId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("excludeId") Long excludeId);

    /**
     * 查询指定时间段内的会议预约
     * @param meetingRoomId 会议室ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 预约列表
     */
    List<MeetingBooking> findBookingsByTimeRange(
            @Param("meetingRoomId") Long meetingRoomId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);

    /**
     * 查询用户的预约列表
     * @param userId 用户ID
     * @return 预约列表
     */
    List<MeetingBooking> findBookingsByUserId(@Param("userId") Long userId);

    /**
     * 统计会议室的预约次数
     * @param meetingRoomId 会议室ID
     * @return 预约次数
     */
    Integer countBookingsByMeetingRoomId(@Param("meetingRoomId") Long meetingRoomId);

    /**
     * 统计会议室在指定时间段内的预约次数
     * @param meetingRoomId 会议室ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 预约次数
     */
    Integer countBookingsByMeetingRoomIdAndTimeRange(
            @Param("meetingRoomId") Long meetingRoomId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);

    /**
     * 统计会议室的已完成会议次数
     * @param meetingRoomId 会议室ID
     * @return 已完成会议次数
     */
    Integer countCompletedMeetingsByMeetingRoomId(@Param("meetingRoomId") Long meetingRoomId);

    /**
     * 统计会议室的取消会议次数
     * @param meetingRoomId 会议室ID
     * @return 取消会议次数
     */
    Integer countCancelledMeetingsByMeetingRoomId(@Param("meetingRoomId") Long meetingRoomId);

    /**
     * 查询会议室的所有已完成会议的签到信息
     * @param meetingRoomId 会议室ID
     * @return 签到信息列表
     */
    List<MeetingBooking> findCompletedMeetingsCheckInInfo(@Param("meetingRoomId") Long meetingRoomId);
}