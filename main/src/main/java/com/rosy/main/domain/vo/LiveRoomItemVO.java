package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class LiveRoomItemVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long liveRoomId;

    private Long itemId;

    private ItemVO item;

    private String sellingPoints;

    private Integer sortOrder;

    private Byte status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}