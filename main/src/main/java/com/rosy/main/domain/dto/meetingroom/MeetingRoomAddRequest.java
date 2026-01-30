package com.rosy.main.domain.dto.meetingroom;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class MeetingRoomAddRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "会议室名称不能为空")
    private String name;

    @NotBlank(message = "会议室位置不能为空")
    private String location;

    @NotNull(message = "会议室容量不能为空")
    @Min(value = 1, message = "会议室容量至少为1")
    private Integer capacity;

    private String equipment;

    private String description;
}
