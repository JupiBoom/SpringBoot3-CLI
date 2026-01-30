package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class LiveRoomVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String roomName;

    private String roomDescription;

    private String coverImage;

    private Long anchorId;

    private String anchorName;

    private Byte status;

    private LocalDateTime plannedStartTime;

    private LocalDateTime actualStartTime;

    private LocalDateTime endTime;

    private Long currentProductId;

    private Long creatorId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
