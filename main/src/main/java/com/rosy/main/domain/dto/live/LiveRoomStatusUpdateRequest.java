package com.rosy.main.domain.dto.live;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 直播间状态更新请求
 * </p>
 *
 * @author Rosy
 * @since 2026-01-16
 */
@Data
public class LiveRoomStatusUpdateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 直播间ID
     */
    private Long liveRoomId;

    /**
     * 目标状态：0-未开播，1-直播中，2-已结束，3-暂停
     */
    private Byte targetStatus;

    /**
     * 当前讲解商品ID（可选，切换讲解商品时使用）
     */
    private Long currentProductId;
}