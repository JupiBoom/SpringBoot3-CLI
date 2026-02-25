package com.rosy.main.domain.dto.liveroom;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class LiveRoomUpdateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "直播间ID不能为空")
    @Positive(message = "直播间ID必须为正整数")
    private Long id;

    @Size(max = 200, message = "标题长度不能超过200个字符")
    private String title;

    @Size(max = 500, message = "封面URL长度不能超过500个字符")
    private String coverUrl;

    @Size(max = 100, message = "主播名称长度不能超过100个字符")
    private String anchorName;
}
