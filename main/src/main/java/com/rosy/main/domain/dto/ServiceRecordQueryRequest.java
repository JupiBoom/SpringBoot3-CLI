package com.rosy.main.domain.dto;

import com.rosy.common.domain.entity.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * 服务记录查询请求DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ServiceRecordQueryRequest extends PageRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 活动ID
     */
    private Long activityId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 服务日期（开始）
     */
    private LocalDate startDate;

    /**
     * 服务日期（结束）
     */
    private LocalDate endDate;

    /**
     * 是否已生成证明
     */
    private Integer certificateGenerated;
}
