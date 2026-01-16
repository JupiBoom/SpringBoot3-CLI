package com.rosy.main.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class LiveRoomVO {
    private Long id;
    private String name;
    private String description;
    private Integer status;
    private String coverUrl;
    private String streamUrl;
    private Long currentGoodsId;
    private Long creatorId;
    private LocalDateTime createTime;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime updateTime;
    private List<LiveGoodsVO> goodsList;
    private LiveDataVO liveData;
}
