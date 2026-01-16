package com.rosy.main.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MeetingCheckinVO {

    private Long id;
    private Long reservationId;
    private Long userId;
    private String userName;
    private LocalDateTime checkinTime;
    private Byte checkinType;
    private String checkinTypeDesc;
}
