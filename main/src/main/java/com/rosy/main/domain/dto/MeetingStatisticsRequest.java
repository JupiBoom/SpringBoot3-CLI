package com.rosy.main.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MeetingStatisticsRequest {

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long roomId;
}
