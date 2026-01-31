package com.rosy.main.domain.dto;

import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;

@Data
public class RepairRecordRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "工单ID不能为空")
    private Long orderId;

    @NotNull(message = "操作类型不能为空")
    private Byte actionType;

    private String content;

    private String photoUrls;

    private String location;
}
