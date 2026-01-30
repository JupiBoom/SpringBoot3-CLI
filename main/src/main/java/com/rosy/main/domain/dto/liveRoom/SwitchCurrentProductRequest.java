package com.rosy.main.domain.dto.liveRoom;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 切换当前讲解商品请求
 * </p>
 *
 * @author Rosy
 * @since 2025-01-30
 */
@Data
public class SwitchCurrentProductRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 直播间ID
     */
    private Long liveRoomId;

    /**
     * 商品ID
     */
    private Long productId;
}