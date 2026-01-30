package com.rosy.main.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("live_room_audience")
public class LiveRoomAudience implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long liveRoomId;

    private Long userId;

    private LocalDateTime enterTime;

    private LocalDateTime leaveTime;

    private Integer stayDuration;

    private Byte isPurchase;
}