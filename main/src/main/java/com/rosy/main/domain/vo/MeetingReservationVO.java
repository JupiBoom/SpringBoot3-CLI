package com.rosy.main.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MeetingReservationVO {

    private Long id;
    private Long roomId;
    private String roomName;
    private String title;
    private String reason;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long applicantId;
    private String applicantName;
    private Byte status;
    private String statusDesc;
    private Integer attendees;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
