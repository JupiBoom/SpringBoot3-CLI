package com.rosy.main.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 维修记录创建DTO
 */
@Data
public class RepairRecordCreateDTO {

    @NotNull(message = "工单ID不能为空")
    private Long orderId;

    @NotBlank(message = "记录类型不能为空")
    private String recordType;

    @NotBlank(message = "记录内容不能为空")
    private String content;

    private List<String> images;
}
