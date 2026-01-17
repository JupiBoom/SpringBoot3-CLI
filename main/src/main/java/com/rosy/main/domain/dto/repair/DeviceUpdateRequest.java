package com.rosy.main.domain.dto.repair;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class DeviceUpdateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "设备ID不能为空")
    private Long id;

    @NotBlank(message = "设备编号不能为空")
    private String deviceNo;

    @NotBlank(message = "设备名称不能为空")
    private String deviceName;

    @NotBlank(message = "设备类型不能为空")
    private String deviceType;

    @NotBlank(message = "设备位置不能为空")
    private String location;

    private String brand;

    private String model;

    private String specifications;

    private Integer status;

    private String remark;
}
