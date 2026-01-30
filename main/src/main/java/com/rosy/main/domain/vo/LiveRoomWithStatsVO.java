package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class LiveRoomWithStatsVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private LiveRoomVO roomInfo;

    private LiveRoomStatsVO stats;

    private List<LiveProductVO> products;
}
