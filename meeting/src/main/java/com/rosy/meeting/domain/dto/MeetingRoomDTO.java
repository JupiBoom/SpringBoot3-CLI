package com.rosy.meeting.domain.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class MeetingRoomDTO {
    private Long id;
    
    @NotBlank(message = "会议室名称不能为空")
    private String roomName;
    
    @NotBlank(message = "位置不能为空")
    private String location;
    
    @NotNull(message = "容量不能为空")
    private Integer capacity;
    
    private String equipment;
    
    private String description;
    
    private Integer status;
}