package com.rosy.main.domain.dto.repair;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 维修记录添加请求
 *
 * @author Rosy
 * @since 2025-01-31
 */
@Data
public class RepairRecordAddRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 工单ID
     */
    private Long orderId;

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
    private String recordTime;
}