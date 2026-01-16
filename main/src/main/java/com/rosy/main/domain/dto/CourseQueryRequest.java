package com.rosy.main.domain.dto;

import com.rosy.common.domain.entity.PageRequest;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class CourseQueryRequest extends PageRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 课程ID
     */
    @Positive(message = "课程ID必须为正整数")
    private Long id;

    /**
     * 课程名称
     */
    @Size(max = 100, message = "课程名称长度不能超过100个字符")
    private String courseName;

    /**
     * 教师ID
     */
    @Positive(message = "教师ID必须为正整数")
    private Long teacherId;

    /**
     * 课程简介
     */
    @Size(max = 500, message = "课程简介长度不能超过500个字符")
    private String description;
}
