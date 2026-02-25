package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 直播间视图对象
 */
@Data
public class LiveRoomVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 直播间ID
     */
    private Long id;

    /**
     * 直播间标题
     */
    private String title;

    /**
     * 直播间封面图URL
     */
    private String coverImage;

    /**
     * 主播ID
     */
    private Long streamerId;

    /**
     * 主播名称
     */
    private String streamerName;

    /**
     * 直播状态：0-未开始，1-直播中，2-已结束，3-已禁播
     */
    private Byte status;

    /**
     * 直播状态文本
     */
    private String statusText;

    /**
     * 直播开始时间
     */
    private LocalDateTime startTime;

    /**
     * 直播结束时间
     */
    private LocalDateTime endTime;

    /**
     * 直播间简介
     */
    private String roomDesc;

    /**
     * 当前讲解商品ID
     */
    private Long currentItemId;

    /**
     * 当前讲解商品信息
     */
    private LiveRoomItemVO currentItem;

    /**
     * 当前观众人数
     */
    private Integer viewerCount;

    /**
     * 累计观看人数
     */
    private Integer totalViewers;

    /**
     * 点赞数
     */
    private Integer likeCount;

    /**
     * 订单数
     */
    private Integer orderCount;

    /**
     * 销售额
     */
    private BigDecimal salesAmount;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
