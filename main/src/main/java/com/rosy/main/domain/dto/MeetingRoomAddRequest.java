package com.rosy.main.domain.dto;

import lombok.Data;

@Data
public class MeetingRoomAddRequest {

    private String name;
    private String location;
    private Integer capacity;
    private String equipment;
    private Byte status;
}
