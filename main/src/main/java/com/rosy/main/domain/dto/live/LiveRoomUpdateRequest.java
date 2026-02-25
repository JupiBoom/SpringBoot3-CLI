package com.rosy.main.domain.dto.live;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class LiveRoomUpdateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String roomName;

    private String roomCover;

    private String anchorName;

    private String anchorAvatar;

    private LocalDateTime scheduledTime;
}
