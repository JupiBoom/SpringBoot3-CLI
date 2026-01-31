package com.rosy.main.domain.dto.attendance;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * <p>
 * 考勤更新请求对象
 * </p>
 *
 * @author Rosy
 * @since 2025-01-30
 */
@Data
public class AttendanceUpdateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 考勤ID
     */
    private Long id;

    /**
     * 课程ID
     */
    private Long courseId;

    /**
     * 学生ID
     */
    private Long studentId;

    /**
     * 考勤日期
     */
    private LocalDate attendanceDate;

    /**
     * 考勤状态：0-缺勤，1-出勤，2-请假
     */
    private Byte status;

    /**
     * 备注
     */
    private String remark;
}