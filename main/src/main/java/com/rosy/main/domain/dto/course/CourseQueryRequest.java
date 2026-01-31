package com.rosy.main.domain.dto.course;

import com.rosy.common.domain.entity.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 课程查询请求对象
 * </p>
 *
 * @author Rosy
 * @since 2025-01-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CourseQueryRequest extends PageRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 课程名称
     */
    private String courseName;

    /**
     * 教师ID
     */
    private Long teacherId;

    /**
     * 教室
     */
    private String classroom;

    /**
     * 状态：0-停用，1-启用
     */
    private Byte status;
}