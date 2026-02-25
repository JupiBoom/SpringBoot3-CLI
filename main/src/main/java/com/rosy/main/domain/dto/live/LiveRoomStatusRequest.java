package com.rosy.main.domain.dto.live;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 更新直播间状态请求
 */
@Data
public class LiveRoomStatusRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 直播间ID
     */
    @NotNull(message = "直播间ID不能为空")
    private Long roomId;

    /**
     * 直播状态：0-未开始，1-直播中，2-已结束，3-已禁播
     */
    @NotNull(message = "状态不能为空")
    private Byte status;
}
