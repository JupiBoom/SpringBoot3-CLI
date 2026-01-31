package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * <p>
 * 考勤视图对象
 * </p>
 *
 * @author Rosy
 * @since 2025-01-30
 */
@Data
public class AttendanceVO implements Serializable {

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
     * 课程名称
     */
    private String courseName;

    /**
     * 学生ID
     */
    private Long studentId;

    /**
     * 学生姓名
     */
    private String studentName;

    /**
     * 学生学号
     */
    private String studentNo;

    /**
     * 考勤日期
     */
    private LocalDate attendanceDate;

    /**
     * 考勤状态：0-缺勤，1-出勤，2-请假
     */
    private Byte status;

    /**
     * 考勤状态描述
     */
    private String statusDesc;

    /**
     * 备注
     */
    private String remark;
}