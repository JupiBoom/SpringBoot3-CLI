package com.rosy.meeting.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 会议室更新VO
 */
@Schema(description = "会议室更新请求")
public class MeetingRoomUpdateVO {

    /**
     * 会议室ID
     */
    @Schema(description = "会议室ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    /**
     * 会议室名称
     */
    @Schema(description = "会议室名称")
    private String name;

    /**
     * 位置
     */
    @Schema(description = "位置")
    private String location;

    /**
     * 容量
     */
    @Schema(description = "容量")
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public List<String> getEquipment() {
        return equipment;
    }

    public void setEquipment(List<String> equipment) {
        this.equipment = equipment;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}