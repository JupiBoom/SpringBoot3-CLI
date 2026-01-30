package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class LiveRetentionDataVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private LocalDateTime recordTime;

    private Integer minuteOffset;

    private Integer viewerCount;
}
