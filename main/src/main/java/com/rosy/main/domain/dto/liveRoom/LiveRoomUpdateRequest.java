package com.rosy.main.domain.dto.liveRoom;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 直播间更新请求
 * </p>
 *
 * @author Rosy
 * @since 2025-01-30
 */
@Data
public class LiveRoomUpdateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;

    /**
     * 直播间标题
     */
    private String title;

    /**
     * 直播间描述
     */
    private String description;

    /**
     * 直播间封面图URL
     */
    private String coverImage;

    /**
     * 直播流地址
     */
    private String streamUrl;

    /**
     * 直播状态：0-未开始，1-直播中，2-已结束
     */
    private Byte status;

    /**
     * 主播ID
     */
    private Long hostId;

    /**
     * 直播开始时间
     */
    private LocalDateTime startTime;

    /**
     * 直播结束时间
     */
    private LocalDateTime endTime;

    /**
     * 当前讲解的商品ID
     */
    private Long currentProductId;

    /**
     * 当前观众人数
     */
    private Integer viewerCount;

    /**
     * 排序字段，用于控制直播间显示顺序
     */
    private Integer sortOrder;
}