package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class LiveRoomVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String title;

    private String description;

    private Byte status;

    private String statusText;

    private String coverUrl;

    private String streamUrl;

    private Long anchorId;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private List<LiveRoomProductVO> products;

    private LiveRoomDataVO currentData;
}