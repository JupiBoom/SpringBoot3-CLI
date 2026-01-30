package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class LiveRoomRetentionVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long liveRoomId;

    private List<RetentionPoint> retentionPoints;

    @Data
    public static class RetentionPoint {
        private LocalDateTime timePoint;
        private Integer audienceCount;
        private Integer retainedCount;
        private Double retentionRate;
    }
}