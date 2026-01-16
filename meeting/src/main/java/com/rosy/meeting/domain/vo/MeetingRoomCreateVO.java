package com.rosy.meeting.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 会议室创建VO
 */
@Data
@Schema(description = "会议室创建请求")
public class MeetingRoomCreateVO {

    /**
     * 会议室名称
     */
    @Schema(description = "会议室名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    /**
     * 位置
     */
    @Schema(description = "位置", requiredMode = Schema.RequiredMode.REQUIRED)
    private String location;

    /**
     * 容量
     */
    @Schema(description = "容量", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer capacity;

    /**
     * 设备列表
     */
    @Schema(description = "设备列表")
    private List<String> equipment;

    /**
     * 状态：0-可用，1-不可用
     */
    @Schema(description = "状态：0-可用，1-不可用")
    private Integer status;
}
