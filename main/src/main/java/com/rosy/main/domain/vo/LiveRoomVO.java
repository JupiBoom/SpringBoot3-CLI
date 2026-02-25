package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class LiveRoomVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String roomName;

    private String roomCover;

    private String anchorName;

    private String anchorAvatar;

    private Byte roomStatus;

    private String roomStatusText;

    private LocalDateTime scheduledTime;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Long currentProductId;

    private LiveProductVO currentProduct;

    private LiveStatisticsVO statistics;

    private Long creatorId;

    private LocalDateTime createTime;

    private List<LiveProductVO> products;
}
