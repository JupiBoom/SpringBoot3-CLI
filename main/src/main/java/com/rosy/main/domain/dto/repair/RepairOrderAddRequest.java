package com.rosy.main.domain.dto.repair;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class RepairOrderAddRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "设备类型不能为空")
    private String deviceType;

    @NotBlank(message = "设备位置不能为空")
    @Size(max = 200, message = "设备位置长度不能超过200个字符")
    private String location;

    @NotNull(message = "故障类型不能为空")
    private String faultType;

    @NotBlank(message = "故障描述不能为空")
    @Size(max = 1000, message = "故障描述长度不能超过1000个字符")
    private String faultDesc;

    @NotNull(message = "优先级不能为空")
    @Min(value = 1, message = "优先级值不能小于1")
    @Max(value = 3, message = "优先级值不能大于3")
    private Byte priority;

    @NotBlank(message = "报修人姓名不能为空")
    @Size(max = 50, message = "报修人姓名长度不能超过50个字符")
    private String reporterName;

    @NotBlank(message = "报修人电话不能为空")
    @Size(max = 20, message = "报修人电话长度不能超过20个字符")
    private String reporterPhone;

    private List<String> photoUrls;
}
