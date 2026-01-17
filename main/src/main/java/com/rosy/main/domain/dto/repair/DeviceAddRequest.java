package com.rosy.main.domain.dto.repair;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class DeviceAddRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "设备编号不能为空")
    @Size(max = 50, message = "设备编号长度不能超过50个字符")
    private String deviceNo;

    @NotBlank(message = "设备名称不能为空")
    @Size(max = 100, message = "设备名称长度不能超过100个字符")
    private String deviceName;

    @NotBlank(message = "设备类型不能为空")
    @Size(max = 50, message = "设备类型长度不能超过50个字符")
    private String deviceType;

    @Size(max = 50, message = "品牌长度不能超过50个字符")
    private String brand;

    @Size(max = 50, message = "型号长度不能超过50个字符")
    private String model;

    @Size(max = 200, message = "设备位置长度不能超过200个字符")
    private String location;
}
