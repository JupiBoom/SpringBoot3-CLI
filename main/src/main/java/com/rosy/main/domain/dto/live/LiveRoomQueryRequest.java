package com.rosy.main.domain.dto.live;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 直播间查询请求
 * </p>
 *
 * @author Rosy
 * @since 2026-01-16
 */
@Data
public class LiveRoomQueryRequest implements Serializable {

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
     * 直播间状态
     */
    private Byte status;

    /**
     * 主播ID
     */
    private Long anchorId;

    /**
     * 主播名称
     */
    private String anchorName;

    /**
     * 开始时间-起始
     */
    private LocalDateTime startTimeStart;

    /**
     * 开始时间-结束
     */
    private LocalDateTime startTimeEnd;

    /**
     * 创建时间-起始
     */
    private LocalDateTime createTimeStart;

    /**
     * 创建时间-结束
     */
    private LocalDateTime createTimeEnd;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 排序顺序
     */
    private String sortOrder;
}