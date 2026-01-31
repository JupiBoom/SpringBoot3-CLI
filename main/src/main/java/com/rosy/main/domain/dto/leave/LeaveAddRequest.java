package com.rosy.main.domain.dto.leave;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * <p>
 * 请假添加请求对象
 * </p>
 *
 * @author Rosy
 * @since 2025-01-30
 */
@Data
public class LeaveAddRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 学生ID
     */
    private Long studentId;

    /**
     * 课程ID
     */
    private Long courseId;

    /**
     * 请假开始日期
     */
    private LocalDate startDate;

    /**
     * 请假结束日期
     */
    private LocalDate endDate;

    /**
     * 请假原因
     */
    private String reason;
}