package com.rosy.main.domain.dto.course;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 课程添加请求对象
 * </p>
 *
 * @author Rosy
 * @since 2025-01-30
 */
@Data
public class CourseAddRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 课程名称
     */
    private String courseName;

    /**
     * 学分
     */
    private BigDecimal credit;

    /**
     * 教师ID
     */
    private Long teacherId;

    /**
     * 教室
     */
    private String classroom;

    /**
     * 课程描述
     */
    private String description;
}