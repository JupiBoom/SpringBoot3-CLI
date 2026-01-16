package com.rosy.main.domain.vo;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
public class LiveRoomCreateVO {
    @NotBlank(message = "直播间名称不能为空")
    @Size(max = 100, message = "直播间名称不能超过100个字符")
    private String name;

    @Size(max = 500, message = "直播间描述不能超过500个字符")
    private String description;

    private String coverUrl;
    private String streamUrl;
}
