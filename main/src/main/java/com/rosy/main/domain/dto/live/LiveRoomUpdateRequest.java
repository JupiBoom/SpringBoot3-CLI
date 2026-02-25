package com.rosy.main.domain.dto.live;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 更新直播间请求
 */
@Data
public class LiveRoomUpdateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 直播间ID
     */
    @NotNull(message = "直播间ID不能为空")
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
     * 直播间简介
     */
    private String roomDesc;
}
