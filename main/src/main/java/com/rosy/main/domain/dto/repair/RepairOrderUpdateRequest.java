package com.rosy.main.domain.dto.repair;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 报修工单更新请求
 *
 * @author Rosy
 * @since 2025-01-31
 */
@Data
public class RepairOrderUpdateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 工单ID
     */
    private Long id;

    /**
     * 设备类型ID
     */
    private Long deviceTypeId;

    /**
     * 故障类型ID
     */
    private Long faultTypeId;

    /**
     * 设备位置
     */
    private String deviceLocation;

    /**
     * 故障描述
     */
    private String faultDescription;

    /**
     * 故障照片URL列表
     */
    private List<String> faultImages;

    /**
     * 报修状态：0-待处理，1-维修中，2-已完成
     */
    private Byte status;

    /**
     * 优先级：1-低，2-中，3-高，4-紧急
     */
    private Byte priority;

    /**
     * 分配给的维修人员ID
     */
    private Long assignedTo;

    /**
     * 分配时间
     */
    private LocalDateTime assignedTime;

    /**
     * 接单时间
     */
    private LocalDateTime acceptedTime;

    /**
     * 完成时间
     */
    private LocalDateTime completedTime;
}