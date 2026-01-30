package com.rosy.main.domain.dto.meetingroom;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class MeetingRoomUpdateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "会议室ID不能为空")
    private Long id;

    private String name;

    private String location;

    @Min(value = 1, message = "会议室容量至少为1")
    private Integer capacity;

    private String equipment;

    private String description;

    private Byte status;
}
