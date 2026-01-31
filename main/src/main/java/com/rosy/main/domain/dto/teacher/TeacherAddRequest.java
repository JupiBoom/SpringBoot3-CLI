package com.rosy.main.domain.dto.teacher;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class TeacherAddRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "教师姓名不能为空")
    @Size(max = 50, message = "教师姓名长度不能超过50个字符")
    private String teacherName;

    @NotBlank(message = "工号不能为空")
    @Size(max = 50, message = "工号长度不能超过50个字符")
    private String teacherNo;

    @NotBlank(message = "性别不能为空")
    @Pattern(regexp = "^[男女]$", message = "性别只能为男或女")
    private String gender;

    @Size(max = 100, message = "学院长度不能超过100个字符")
    private String department;

    @Size(max = 100, message = "职称长度不能超过100个字符")
    private String title;

    @Email(message = "邮箱格式不正确")
    @Size(max = 100, message = "邮箱长度不能超过100个字符")
    private String email;

    @Size(max = 20, message = "电话长度不能超过20个字符")
    private String phone;
}
