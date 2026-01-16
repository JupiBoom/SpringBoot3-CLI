package com.rosy.main.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MeetingCheckinRequest {

    private Long reservationId;
    private LocalDateTime checkinTime;
}
