package com.rosy.main.domain.dto.attendance;

import com.rosy.common.domain.entity.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * <p>
 * 考勤查询请求对象
 * </p>
 *
 * @author Rosy
 * @since 2025-01-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AttendanceQueryRequest extends PageRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 课程ID
     */
    private Long courseId;

    /**
     * 学生ID
     */
    private Long studentId;

    /**
     * 考勤开始日期
     */
    private LocalDate startDate;

    /**
     * 考勤结束日期
     */
    private LocalDate endDate;

    /**
     * 考勤状态：0-缺勤，1-出勤，2-请假
     */
    private Byte status;
}