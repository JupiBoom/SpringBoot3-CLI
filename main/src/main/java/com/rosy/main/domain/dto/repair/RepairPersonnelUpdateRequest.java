package com.rosy.main.domain.dto.repair;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class RepairPersonnelUpdateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "维修人员ID不能为空")
    private Long id;

    @NotBlank(message = "姓名不能为空")
    private String name;

    @NotBlank(message = "工号不能为空")
    private String employeeNo;

    @NotBlank(message = "专业领域不能为空")
    private String specialty;

    private Integer skillLevel;

    private String phone;

    private String email;

    private Integer status;

    private String remark;
}
