package com.rosy.main.domain.dto.course;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class CourseAddRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "课程名称不能为空")
    @Size(max = 100, message = "课程名称长度不能超过100个字符")
    private String courseName;

    @NotBlank(message = "课程代码不能为空")
    @Size(max = 50, message = "课程代码长度不能超过50个字符")
    private String courseCode;

    @NotNull(message = "学分不能为空")
    @DecimalMin(value = "0.0", message = "学分不能小于0")
    @DecimalMax(value = "10.0", message = "学分不能大于10")
    private BigDecimal credits;

    @NotNull(message = "教师ID不能为空")
    private Long teacherId;

    @Size(max = 50, message = "教室长度不能超过50个字符")
    private String classroom;

    @Size(max = 100, message = "上课时间长度不能超过100个字符")
    private String schedule;

    @Size(max = 500, message = "课程描述长度不能超过500个字符")
    private String description;

    @Max(value = 999, message = "最大人数不能超过999")
    private Integer maxStudents;
}
