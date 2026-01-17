package com.rosy.main.domain.dto.repair;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class RepairPersonnelAddRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @NotBlank(message = "姓名不能为空")
    @Size(max = 50, message = "姓名长度不能超过50个字符")
    private String name;

    @Size(max = 20, message = "联系电话长度不能超过20个字符")
    private String phone;

    @Size(max = 100, message = "专业特长长度不能超过100个字符")
    private String specialty;

    @NotNull(message = "技能等级不能为空")
    private Byte skillLevel;
}
