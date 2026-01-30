package com.rosy.main.domain.dto.liveRoom;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 直播间添加请求
 * </p>
 *
 * @author Rosy
 * @since 2025-01-30
 */
@Data
public class LiveRoomAddRequest implements Serializable {

    private static final long serialVersionUID = 1L;

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
     * 主播ID
     */
    private Long hostId;

    /**
     * 直播开始时间
     */
    private LocalDateTime startTime;

    /**
     * 排序字段，用于控制直播间显示顺序
     */
    private Integer sortOrder;
}