package com.rosy.main.domain.dto.liveroom;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class AudienceRecordRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "直播间ID不能为空")
    @Positive(message = "直播间ID必须为正整数")
    private Long liveRoomId;

    @NotNull(message = "观众人数不能为空")
    @PositiveOrZero(message = "观众人数必须为非负整数")
    private Integer viewerCount;

    @PositiveOrZero(message = "新增观众数必须为非负整数")
    private Integer newViewerCount = 0;

    @PositiveOrZero(message = "离开观众数必须为非负整数")
    private Integer leaveViewerCount = 0;
}
