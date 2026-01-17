package com.rosy.main.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 报修工单创建DTO
 */
@Data
public class RepairOrderCreateDTO {

    @NotNull(message = "设备ID不能为空")
    private Long equipmentId;

    @NotBlank(message = "故障类型不能为空")
    private String faultType;

    @NotBlank(message = "故障描述不能为空")
    private String faultDescription;

    private List<String> faultImages;

    private String priority;

    private String remark;
}
