package com.rosy.main.domain.dto.repair;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class RepairOrderCompleteRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "报修单ID不能为空")
    private Long orderId;

    @NotBlank(message = "维修结果不能为空")
    @Size(max = 1000, message = "维修结果长度不能超过1000个字符")
    private String repairResult;
}
