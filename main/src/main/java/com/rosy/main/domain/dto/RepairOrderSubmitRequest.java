package com.rosy.main.domain.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import java.io.Serial;
import java.io.Serializable;

@Data
public class RepairOrderSubmitRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "设备类型不能为空")
    private String deviceType;

    @NotBlank(message = "设备位置不能为空")
    private String deviceLocation;

    @NotBlank(message = "故障类型不能为空")
    private String faultType;

    @NotBlank(message = "故障描述不能为空")
    private String description;

    private String photoUrls;

    private Byte priority;
}
