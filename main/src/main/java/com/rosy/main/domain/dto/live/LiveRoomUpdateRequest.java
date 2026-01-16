package com.rosy.main.domain.dto.live;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 直播间更新请求
 * </p>
 *
 * @author Rosy
 * @since 2026-01-16
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
     * 直播间封面图URL
     */
    private String coverImage;

    /**
     * 主播名称
     */
    private String anchorName;

    /**
     * 预计直播时长（分钟）
     */
    private Integer expectedDuration;

    /**
     * 直播间描述
     */
    private String description;
}