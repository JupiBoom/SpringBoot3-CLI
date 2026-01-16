package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 直播间VO
 * </p>
 *
 * @author Rosy
 * @since 2026-01-16
 */
@Data
public class LiveRoomVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String title;

    private String coverImage;

    private Byte status;

    private String statusText;

    private Long anchorId;

    private String anchorName;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Long currentProductId;

    private Integer expectedDuration;

    private String description;

    private Integer viewerCount;

    private Integer peakViewerCount;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private List<LiveProductVO> products;

    private LiveStatsVO stats;
}