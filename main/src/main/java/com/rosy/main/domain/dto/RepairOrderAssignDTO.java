package com.rosy.main.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 工单分配DTO
 */
@Data
public class RepairOrderAssignDTO {

    @NotNull(message = "工单ID不能为空")
    private Long orderId;

    @NotNull(message = "维修人员ID不能为空")
    private Long repairmanId;

    private String remark;
}
