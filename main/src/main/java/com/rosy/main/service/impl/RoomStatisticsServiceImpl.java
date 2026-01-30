package com.rosy.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.main.domain.entity.Booking;
import com.rosy.main.domain.entity.CheckIn;
import com.rosy.main.domain.entity.MeetingRoom;
import com.rosy.main.domain.entity.RoomStatistics;
import com.rosy.main.domain.vo.RoomStatisticsVO;
import com.rosy.main.mapper.BookingMapper;
import com.rosy.main.mapper.CheckInMapper;
import com.rosy.main.mapper.MeetingRoomMapper;
import com.rosy.main.mapper.RoomStatisticsMapper;
import com.rosy.main.service.IRoomStatisticsService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class RoomStatisticsServiceImpl extends ServiceImpl<RoomStatisticsMapper, RoomStatistics> implements IRoomStatisticsService {

    @Resource
    private BookingMapper bookingMapper;

    @Resource
    private CheckInMapper checkInMapper;

    @Resource
    private MeetingRoomMapper meetingRoomMapper;

    @Override
    public RoomStatisticsVO getRoomStatistics(Long roomId, LocalDate date) {
        MeetingRoom room = meetingRoomMapper.selectById(roomId);
        if (room == null) {
            return null;
        }

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        LambdaQueryWrapper<Booking> bookingWrapper = new LambdaQueryWrapper<>();
        bookingWrapper.eq(Booking::getRoomId, roomId)
                .ge(Booking::getCreateTime, startOfDay)
                .le(Booking::getCreateTime, endOfDay);
        List<Booking> bookings = bookingMapper.selectList(bookingWrapper);

        int totalBookings = bookings.size();
        int approvedCount = 0, rejectedCount = 0, cancelledCount = 0, completedCount = 0;
        long totalMinutes = 0;

        for (Booking booking : bookings) {
            if (booking.getStatus() == 1) approvedCount++;
            else if (booking.getStatus() == 2) rejectedCount++;
            else if (booking.getStatus() == 3) cancelledCount++;
            else if (booking.getStatus() == 4) completedCount++;

            if (booking.getStartTime() != null && booking.getEndTime() != null) {
                totalMinutes += ChronoUnit.MINUTES.between(booking.getStartTime(), booking.getEndTime());
            }
        }

        LambdaQueryWrapper<CheckIn> checkInWrapper = new LambdaQueryWrapper<>();
        checkInWrapper.eq(CheckIn::getBookingId, roomId)
                .ge(CheckIn::getCreateTime, startOfDay)
                .le(CheckIn::getCreateTime, endOfDay)
                .eq(CheckIn::getCheckInStatus, 1);
        int totalCheckIns = checkInMapper.selectCount(checkInWrapper).intValue();

        RoomStatisticsVO vo = new RoomStatisticsVO();
        vo.setRoomId(roomId);
        vo.setRoomName(room.getName());
        vo.setStatisticsDate(date);
        vo.setTotalBookings(totalBookings);
        vo.setApprovedBookings(approvedCount);
        vo.setRejectedBookings(rejectedCount);
        vo.setCancelledBookings(cancelledCount);
        vo.setCompletedBookings(completedCount);
        vo.setUsageHours(BigDecimal.valueOf(totalMinutes / 60.0).setScale(2, RoundingMode.HALF_UP));
        vo.setOccupancyRate(totalMinutes > 0 ? BigDecimal.valueOf(totalMinutes / (24.0 * 60) * 100).setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO);
        vo.setTotalCheckIns(totalCheckIns);
        vo.setActualAttendees(totalCheckIns);

        return vo;
    }

    @Override
    public List<RoomStatisticsVO> getRoomStatisticsRange(Long roomId, LocalDate startDate, LocalDate endDate) {
        List<RoomStatisticsVO> result = new ArrayList<>();
        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            RoomStatisticsVO vo = getRoomStatistics(roomId, currentDate);
            if (vo != null) {
                result.add(vo);
            }
            currentDate = currentDate.plusDays(1);
        }
        return result;
    }

    @Override
    public void generateDailyStatistics(LocalDate date) {
        List<MeetingRoom> rooms = meetingRoomMapper.selectList(null);
        for (MeetingRoom room : rooms) {
            RoomStatisticsVO vo = getRoomStatistics(room.getId(), date);
            if (vo != null) {
                RoomStatistics statistics = new RoomStatistics();
                statistics.setRoomId(vo.getRoomId());
                statistics.setStatisticsDate(vo.getStatisticsDate());
                statistics.setTotalBookings(vo.getTotalBookings());
                statistics.setApprovedBookings(vo.getApprovedBookings());
                statistics.setRejectedBookings(vo.getRejectedBookings());
                statistics.setCancelledBookings(vo.getCancelledBookings());
                statistics.setCompletedBookings(vo.getCompletedBookings());
                statistics.setUsageHours(vo.getUsageHours());
                statistics.setOccupancyRate(vo.getOccupancyRate());
                statistics.setTotalCheckIns(vo.getTotalCheckIns());
                statistics.setActualAttendees(vo.getActualAttendees());
                this.save(statistics);
            }
        }
    }
}
