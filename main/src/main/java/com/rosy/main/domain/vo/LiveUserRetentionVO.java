package com.rosy.main.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LiveUserRetentionVO {
    private Long id;
    private Long liveRoomId;
    private LocalDateTime enterTime;
    private LocalDateTime leaveTime;
    private Long userId;
    private Long duration;
    private LocalDateTime createTime;
}
