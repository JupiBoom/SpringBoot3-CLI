package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 维修记录视图对象
 *
 * @author Rosy
 * @since 2025-01-31
 */
@Data
public class RepairRecordVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 记录ID
     */
    private Long id;

    /**
     * 工单ID
     */
    private Long orderId;

    /**
     * 工单号
     */
    private String orderNo;

    /**
     * 维修内容记录
     */
    private String repairContent;

    /**
     * 维修过程照片URL列表
     */
    private List<String> repairImages;

    /**
     * 记录时间
     */
    private LocalDateTime recordTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 维修人员名称
     */
    private String technicianName;
}