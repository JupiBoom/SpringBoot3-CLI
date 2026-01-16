package com.rosy.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rosy.main.domain.dto.MeetingStatisticsRequest;
import com.rosy.main.domain.entity.MeetingCheckin;
import com.rosy.main.domain.entity.MeetingReservation;
import com.rosy.main.domain.entity.MeetingRoom;
import com.rosy.main.domain.enums.ReservationStatusEnum;
import com.rosy.main.domain.vo.MeetingStatisticsVO;
import com.rosy.main.mapper.MeetingCheckinMapper;
import com.rosy.main.mapper.MeetingReservationMapper;
import com.rosy.main.mapper.MeetingRoomMapper;
import com.rosy.main.service.IMeetingStatisticsService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MeetingStatisticsServiceImpl implements IMeetingStatisticsService {

    private final MeetingReservationMapper meetingReservationMapper;
    private final MeetingCheckinMapper meetingCheckinMapper;
    private final MeetingRoomMapper meetingRoomMapper;

    public MeetingStatisticsServiceImpl(MeetingReservationMapper meetingReservationMapper,
                                         MeetingCheckinMapper meetingCheckinMapper,
                                         MeetingRoomMapper meetingRoomMapper) {
        this.meetingReservationMapper = meetingReservationMapper;
        this.meetingCheckinMapper = meetingCheckinMapper;
        this.meetingRoomMapper = meetingRoomMapper;
    }

    @Override
    public MeetingStatisticsVO getStatistics(MeetingStatisticsRequest request) {
        LocalDateTime startDate = request.getStartDate() != null ? request.getStartDate() : LocalDateTime.now().minusMonths(1);
        LocalDateTime endDate = request.getEndDate() != null ? request.getEndDate() : LocalDateTime.now();

        MeetingStatisticsVO statistics = new MeetingStatisticsVO();
        statistics.setStartDate(startDate);
        statistics.setEndDate(endDate);

        QueryWrapper<MeetingReservation> wrapper = new QueryWrapper<>();
        wrapper.ge("create_time", startDate);
        wrapper.le("create_time", endDate);

        if (request.getRoomId() != null) {
            wrapper.eq("room_id", request.getRoomId());
        }

        List<MeetingReservation> reservations = meetingReservationMapper.selectList(wrapper);

        long totalReservations = reservations.size();
        long approvedReservations = reservations.stream()
                .filter(r -> r.getStatus().equals(ReservationStatusEnum.APPROVED.getCode().byteValue()))
                .count();
        long rejectedReservations = reservations.stream()
                .filter(r -> r.getStatus().equals(ReservationStatusEnum.REJECTED.getCode().byteValue()))
                .count();
        long cancelledReservations = reservations.stream()
                .filter(r -> r.getStatus().equals(ReservationStatusEnum.CANCELLED.getCode().byteValue()))
                .count();

        statistics.setTotalReservations(totalReservations);
        statistics.setApprovedReservations(approvedReservations);
        statistics.setRejectedReservations(rejectedReservations);
        statistics.setCancelledReservations(cancelledReservations);

        List<Long> approvedReservationIds = reservations.stream()
                .filter(r -> r.getStatus().equals(ReservationStatusEnum.APPROVED.getCode().byteValue()))
                .map(MeetingReservation::getId)
                .toList();

        QueryWrapper<MeetingCheckin> checkinWrapper = new QueryWrapper<>();
        checkinWrapper.in("reservation_id", approvedReservationIds);
        long totalCheckins = meetingCheckinMapper.selectCount(checkinWrapper);
        statistics.setTotalCheckins(totalCheckins);

        double averageAttendance = approvedReservations > 0 ? (double) totalCheckins / approvedReservations : 0.0;
        statistics.setAverageAttendance(averageAttendance);

        Map<Long, Long> roomUsageCount = new HashMap<>();
        for (MeetingReservation reservation : reservations) {
            if (reservation.getStatus().equals(ReservationStatusEnum.APPROVED.getCode().byteValue())) {
                roomUsageCount.merge(reservation.getRoomId(), 1L, Long::sum);
            }
        }

        Long mostUsedRoomId = roomUsageCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        if (mostUsedRoomId != null) {
            MeetingRoom mostUsedRoom = meetingRoomMapper.selectById(mostUsedRoomId);
            statistics.setMostUsedRoomId(mostUsedRoomId);
            statistics.setMostUsedRoomName(mostUsedRoom != null ? mostUsedRoom.getName() : null);
        }

        return statistics;
    }
}
