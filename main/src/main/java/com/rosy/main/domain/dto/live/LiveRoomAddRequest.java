package com.rosy.main.domain.dto.live;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 直播间创建请求
 * </p>
 *
 * @author Rosy
 * @since 2026-01-16
 */
@Data
public class LiveRoomAddRequest implements Serializable {

    private static final long serialVersionUID = 1L;

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
    private Long anchorId;

    /**
     * 主播名称
     */
    private String anchorName;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 预计直播时长（分钟）
     */
    private Integer expectedDuration;

    /**
     * 直播间描述
     */
    private String description;
}