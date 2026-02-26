package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class LiveRoomVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String title;

    private String coverUrl;

    private Long anchorId;

    private String anchorName;

    private Byte status;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Long currentProductId;

    private Integer viewerCount;

    private Integer totalViewerCount;

    private BigDecimal totalSales;

    private Integer totalOrders;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
