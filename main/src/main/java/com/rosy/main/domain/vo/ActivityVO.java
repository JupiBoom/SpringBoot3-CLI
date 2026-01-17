package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class ActivityVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String title;

    private String description;

    private Byte category;

    private String categoryName;

    private Byte status;

    private String statusName;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String location;

    private Integer requiredPeople;

    private Integer currentPeople;

    private Long creatorId;

    private String creatorName;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
