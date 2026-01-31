package com.rosy.main.domain.dto.repair;

import com.rosy.common.domain.entity.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 报修工单查询请求
 *
 * @author Rosy
 * @since 2025-01-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RepairOrderQueryRequest extends PageRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 工单号
     */
    private String orderNo;

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
     * 报修状态：0-待处理，1-维修中，2-已完成
     */
    private Byte status;

    /**
     * 优先级：1-低，2-中，3-高，4-紧急
     */
    private Byte priority;

    /**
     * 报修用户ID
     */
    private Long userId;

    /**
     * 分配给的维修人员ID
     */
    private Long assignedTo;

    /**
     * 创建时间开始
     */
    private LocalDateTime createTimeBegin;

    /**
     * 创建时间结束
     */
    private LocalDateTime createTimeEnd;
}