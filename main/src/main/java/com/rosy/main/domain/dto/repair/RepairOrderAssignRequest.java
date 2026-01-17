package com.rosy.main.domain.dto.repair;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class RepairOrderAssignRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "报修单ID不能为空")
    private Long orderId;

    @NotNull(message = "维修人员ID不能为空")
    private Long assigneeId;
}
