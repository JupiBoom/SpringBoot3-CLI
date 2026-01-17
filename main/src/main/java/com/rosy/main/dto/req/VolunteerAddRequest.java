package com.rosy.main.dto.req;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class VolunteerAddRequest {

    @NotBlank(message = "姓名不能为空")
    @Size(max = 20, message = "姓名不能超过20字")
    private String name;
    
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;
    
    @Email(message = "邮箱格式不正确")
    private String email;
    
    private Integer gender;
    
    @NotNull(message = "年龄不能为空")
    private Integer age;
    
    private String avatar;
}