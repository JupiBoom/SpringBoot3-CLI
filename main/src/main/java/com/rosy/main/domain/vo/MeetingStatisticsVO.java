package com.rosy.main.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MeetingStatisticsVO {

    private Long totalReservations;
    private Long approvedReservations;
    private Long rejectedReservations;
    private Long cancelledReservations;
    private Long totalCheckins;
    private Double averageAttendance;
    private Long mostUsedRoomId;
    private String mostUsedRoomName;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
