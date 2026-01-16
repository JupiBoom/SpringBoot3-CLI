package com.rosy.meeting.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.meeting.domain.entity.MeetingBooking;
import com.rosy.meeting.domain.vo.MeetingBookingApproveVO;
import com.rosy.meeting.domain.vo.MeetingBookingCreateVO;
import com.rosy.meeting.domain.vo.MeetingBookingVO;
import com.rosy.meeting.domain.vo.MeetingRoomStatisticsVO;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 会议预约服务接口
 */
public interface IMeetingBookingService extends IService<MeetingBooking> {

    /**
     * 提交预约申请
     * @param vo 预约创建请求
     * @return 预约VO
     */
    MeetingBookingVO createBooking(MeetingBookingCreateVO vo);

    /**
     * 审批预约
     * @param vo 审批请求
     * @return 预约VO
     */
    MeetingBookingVO approveBooking(MeetingBookingApproveVO vo);

    /**
     * 获取预约详情
     * @param id 预约ID
     * @return 预约VO
     */
    MeetingBookingVO getBookingById(Long id);

    /**
     * 获取用户的预约列表
     * @param userId 用户ID
     * @return 预约VO列表
     */
    List<MeetingBookingVO> getBookingsByUserId(Long userId);

    /**
     * 获取待审批的预约列表
     * @return 预约VO列表
     */
    List<MeetingBookingVO> getPendingBookings();

    /**
     * 获取会议室的预约列表
     * @param meetingRoomId 会议室ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 预约VO列表
     */
    List<MeetingBookingVO> getBookingsByMeetingRoom(Long meetingRoomId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 取消预约
     * @param id 预约ID
     */
    void cancelBooking(Long id);

    /**
     * 会议签到
     * @param id 预约ID
     * @param userId 用户ID
     */
    void checkIn(Long id, Long userId);

    /**
     * 获取会议室使用统计
     * @param meetingRoomId 会议室ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 使用次数
     */
    int getUsageStatistics(Long meetingRoomId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取会议室详细统计信息
     * @param meetingRoomId 会议室ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 会议室统计VO
     */
    MeetingRoomStatisticsVO getMeetingRoomStatistics(Long meetingRoomId, LocalDateTime startTime, LocalDateTime endTime);
}