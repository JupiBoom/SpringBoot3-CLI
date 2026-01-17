package com.rosy.volunteer.domain.dto;

import lombok.Data;

import javax.validation.constraints.*;

@Data
public class RegistrationCreateDTO {
    @NotNull(message = "活动ID不能为空")
    private Long activityId;
    @NotBlank(message = "真实姓名不能为空")
    @Size(max = 50, message = "真实姓名不能超过50个字符")
    private String realName;
    @NotBlank(message = "联系电话不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;
    @Email(message = "邮箱格式不正确")
    @Size(max = 100, message = "邮箱不能超过100个字符")
    private String email;
    @Min(value = 16, message = "年龄至少为16岁")
    @Max(value = 70, message = "年龄不能超过70岁")
    private Integer age;
    @Min(value = 1, message = "性别值不正确")
    @Max(value = 2, message = "性别值不正确")
    private Integer gender;
    @Size(max = 500, message = "个人技能不能超过500个字符")
    private String skills;
}
