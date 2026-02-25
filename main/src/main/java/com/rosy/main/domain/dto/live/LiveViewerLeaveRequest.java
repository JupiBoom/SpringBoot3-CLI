package com.rosy.main.domain.dto.live;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 观众离开直播间请求
 */
@Data
public class LiveViewerLeaveRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 直播间ID
     */
    @NotNull(message = "直播间ID不能为空")
    private Long roomId;

    /**
     * 用户ID（未登录可为空）
     */
    private Long userId;
}
