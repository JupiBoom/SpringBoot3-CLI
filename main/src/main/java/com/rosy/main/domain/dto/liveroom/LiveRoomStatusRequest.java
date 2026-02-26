package com.rosy.main.domain.dto.liveroom;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class LiveRoomStatusRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "直播间ID不能为空")
    @Positive(message = "直播间ID必须为正整数")
    private Long liveRoomId;

    @NotNull(message = "状态不能为空")
    @Min(value = 0, message = "状态值只能为0-2")
    @Max(value = 2, message = "状态值只能为0-2")
    private Byte status;
}
