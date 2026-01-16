package com.rosy.meeting.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.meeting.domain.entity.MeetingBooking;
import com.rosy.meeting.domain.vo.MeetingBookingApproveVO;
import com.rosy.meeting.domain.vo.MeetingBookingCreateVO;
import com.rosy.meeting.domain.vo.MeetingBookingVO;
import com.rosy.meeting.domain.vo.MeetingRoomStatisticsVO;
import com.rosy.meeting.mapper.MeetingBookingMapper;
import com.rosy.meeting.service.IMeetingBookingService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 会议预约服务实现类
 */
@Service
public class MeetingBookingServiceImpl extends ServiceImpl<MeetingBookingMapper, MeetingBooking> implements IMeetingBookingService {

    @Override
    public MeetingBookingVO createBooking(MeetingBookingCreateVO vo) {
        // 检查是否有时间冲突
        if (hasTimeConflict(vo.getMeetingRoomId(), vo.getStartTime(), vo.getEndTime(), null)) {
            throw new RuntimeException("该时间段已有预约");
        }

        MeetingBooking booking = new MeetingBooking();
        BeanUtils.copyProperties(vo, booking);
        booking.setStatus(0); // 待审批
        booking.setCreateTime(LocalDateTime.now());
        save(booking);

        // 发送待审批通知
        sendApprovalNotification(booking);

        return toVO(booking);
    }

    @Override
    public MeetingBookingVO approveBooking(MeetingBookingApproveVO vo) {
        MeetingBooking booking = getById(vo.getBookingId());
        if (booking == null) {
            throw new RuntimeException("预约不存在");
        }

        // 检查状态是否为待审批
        if (booking.getStatus() != 0) {
            throw new RuntimeException("该预约已处理");
        }

        // 如果通过，再次检查是否有时间冲突
        if (vo.getApprovalStatus() == 1 && hasTimeConflict(booking.getMeetingRoomId(), booking.getStartTime(), booking.getEndTime(), booking.getId())) {
            throw new RuntimeException("该时间段已有预约");
        }

        booking.setStatus(vo.getApprovalStatus());
        booking.setApproveTime(LocalDateTime.now());
        booking.setApproveRemark(vo.getApprovalRemark());
        updateById(booking);

        // 发送审批结果通知
        sendApprovalResultNotification(booking);

        // 如果通过，发送会议开始前提醒
        if (vo.getApprovalStatus() == 1) {
            sendMeetingReminder(booking);
        }

        return toVO(booking);
    }

    @Override
    public MeetingBookingVO getBookingById(Long id) {
        MeetingBooking booking = getById(id);
        if (booking == null) {
            throw new RuntimeException("预约不存在");
        }
        return toVO(booking);
    }

    @Override
    public List<MeetingBookingVO> getBookingsByUserId(Long userId) {
        LambdaQueryWrapper<MeetingBooking> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MeetingBooking::getUserId, userId);
        wrapper.orderByDesc(MeetingBooking::getCreateTime);
        List<MeetingBooking> bookings = list(wrapper);
        return bookings.stream().map(this::toVO).collect(Collectors.toList());
    }

    @Override
    public List<MeetingBookingVO> getPendingBookings() {
        LambdaQueryWrapper<MeetingBooking> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MeetingBooking::getStatus, 0);
        wrapper.orderByDesc(MeetingBooking::getCreateTime);
        List<MeetingBooking> bookings = list(wrapper);
        return bookings.stream().map(this::toVO).collect(Collectors.toList());
    }

    @Override
    public List<MeetingBookingVO> getBookingsByMeetingRoom(Long meetingRoomId, LocalDateTime startTime, LocalDateTime endTime) {
        List<MeetingBooking> bookings = baseMapper.findBookingsByTimeRange(meetingRoomId, startTime, endTime);
        return bookings.stream().map(this::toVO).collect(Collectors.toList());
    }

    @Override
    public void cancelBooking(Long id) {
        MeetingBooking booking = getById(id);
        if (booking == null) {
            throw new RuntimeException("预约不存在");
        }
        if (booking.getStatus() == 1) {
            throw new RuntimeException("已通过的预约无法取消");
        }
        removeById(id);
    }

    @Override
    public void checkIn(Long id, Long userId) {
        MeetingBooking booking = getById(id);
        if (booking == null) {
            throw new RuntimeException("预约不存在");
        }
        if (!booking.getUserId().equals(userId)) {
            throw new RuntimeException("无权限签到");
        }
        // 记录签到信息
        recordCheckIn(id, userId);
    }

    @Override
    public int getUsageStatistics(Long meetingRoomId, LocalDateTime startTime, LocalDateTime endTime) {
        return baseMapper.findBookingsByTimeRange(meetingRoomId, startTime, endTime).size();
    }

    @Override
    public MeetingRoomStatisticsVO getMeetingRoomStatistics(Long meetingRoomId, LocalDateTime startTime, LocalDateTime endTime) {
        // 获取基本统计数据
        Integer totalBookings = baseMapper.countBookingsByMeetingRoomId(meetingRoomId);
        Integer completedMeetings = baseMapper.countCompletedMeetingsByMeetingRoomId(meetingRoomId);
        Integer cancelledMeetings = baseMapper.countCancelledMeetingsByMeetingRoomId(meetingRoomId);
        
        // 获取时间段内的预约数
        Integer timeRangeBookings = baseMapper.countBookingsByMeetingRoomIdAndTimeRange(meetingRoomId, startTime, endTime);
        
        // 计算平均签到率
        Double averageCheckInRate = 0.0;
        if (completedMeetings != null && completedMeetings > 0) {
            List<MeetingBooking> bookings = baseMapper.findCompletedMeetingsCheckInInfo(meetingRoomId);
            if (!bookings.isEmpty()) {
                double totalRate = 0;
                int validMeetings = 0;
                for (MeetingBooking booking : bookings) {
                    if (booking.getParticipantCount() != null && booking.getParticipantCount() > 0) {
                        totalRate += (double) booking.getCheckInCount() / booking.getParticipantCount();
                        validMeetings++;
                    }
                }
                if (validMeetings > 0) {
                    averageCheckInRate = totalRate / validMeetings;
                }
            }
        }
        
        // 获取本月和上月的预约数
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime thisMonthStart = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime thisMonthEnd = now.withDayOfMonth(now.getMonth().length(now.toLocalDate().isLeapYear())).withHour(23).withMinute(59).withSecond(59);
        Integer thisMonthBookings = baseMapper.countBookingsByMeetingRoomIdAndTimeRange(meetingRoomId, thisMonthStart, thisMonthEnd);
        
        LocalDateTime lastMonthStart = now.minusMonths(1).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime lastMonthEnd = now.minusMonths(1).withDayOfMonth(now.minusMonths(1).getMonth().length(now.minusMonths(1).toLocalDate().isLeapYear())).withHour(23).withMinute(59).withSecond(59);
        Integer lastMonthBookings = baseMapper.countBookingsByMeetingRoomIdAndTimeRange(meetingRoomId, lastMonthStart, lastMonthEnd);
        
        // 组装统计数据
        MeetingRoomStatisticsVO vo = new MeetingRoomStatisticsVO();
        vo.setMeetingRoomId(meetingRoomId);
        vo.setTotalBookings(totalBookings);
        vo.setCompletedMeetings(completedMeetings);
        vo.setCancelledMeetings(cancelledMeetings);
        vo.setAverageCheckInRate(averageCheckInRate);
        vo.setThisMonthBookings(thisMonthBookings);
        vo.setLastMonthBookings(lastMonthBookings);
        
        return vo;
    }
    
    /**
     * 检查是否有时间冲突
     */
    private boolean hasTimeConflict(Long meetingRoomId, LocalDateTime startTime, LocalDateTime endTime, Long excludeId) {
        Integer count = baseMapper.countOverlappingBookings(meetingRoomId, startTime, endTime, excludeId);
        return count != null && count > 0;
    }

    /**
     * 发送审批通知
     */
    private void sendApprovalNotification(MeetingBooking booking) {
        // 这里可以集成邮件、短信或推送通知
        System.out.println("发送审批通知：预约ID " + booking.getId() + " 待审批");
    }

    /**
     * 发送审批结果通知
     */
    private void sendApprovalResultNotification(MeetingBooking booking) {
        // 这里可以集成邮件、短信或推送通知
        String result = booking.getStatus() == 1 ? "通过" : "驳回";
        System.out.println("发送审批结果通知：预约ID " + booking.getId() + " 状态：" + result);
    }

    /**
     * 发送会议开始前提醒
     */
    private void sendMeetingReminder(MeetingBooking booking) {
        // 这里可以集成定时任务，在会议开始前发送提醒
        System.out.println("设置会议提醒：预约ID " + booking.getId() + " 将在 " + booking.getStartTime() + " 开始");
    }

    /**
     * 记录签到信息
     */
    private void recordCheckIn(Long bookingId, Long userId) {
        // 这里可以将签到信息保存到数据库
        System.out.println("签到记录：预约ID " + bookingId + " 用户ID " + userId);
    }

    private MeetingBookingVO toVO(MeetingBooking booking) {
        MeetingBookingVO vo = new MeetingBookingVO();
        BeanUtils.copyProperties(booking, vo);
        return vo;
    }
}