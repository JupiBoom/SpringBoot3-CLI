package com.rosy.main.domain.dto.live;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class LiveRoomCreateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String roomName;

    private String roomDescription;

    private String coverImage;

    private Long anchorId;

    private String anchorName;

    private LocalDateTime plannedStartTime;
}
