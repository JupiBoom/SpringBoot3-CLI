package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 报修工单视图对象
 *
 * @author Rosy
 * @since 2025-01-31
 */
@Data
public class RepairOrderVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 工单ID
     */
    private Long id;

    /**
     * 工单号
     */
    private String orderNo;

    /**
     * 设备类型名称
     */
    private String deviceTypeName;

    /**
     * 故障类型名称
     */
    private String faultTypeName;

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
     * 报修状态描述
     */
    private String statusDesc;

    /**
     * 优先级：1-低，2-中，3-高，4-紧急
     */
    private Byte priority;

    /**
     * 优先级描述
     */
    private String priorityDesc;

    /**
     * 报修用户ID
     */
    private Long userId;

    /**
     * 报修用户名称
     */
    private String userName;

    /**
     * 分配给的维修人员ID
     */
    private Long assignedTo;

    /**
     * 维修人员名称
     */
    private String technicianName;

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

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}