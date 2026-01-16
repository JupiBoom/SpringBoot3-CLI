package com.rosy.main.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MeetingReservationAddRequest {

    private Long roomId;
    private String title;
    private String reason;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer attendees;
}
