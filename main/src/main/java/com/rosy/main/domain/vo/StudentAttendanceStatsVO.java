package com.rosy.main.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class StudentAttendanceStatsVO {

    private Long studentId;

    private String studentName;

    private Long courseId;

    private String courseName;

    private Integer totalClasses;

    private Integer attendedClasses;

    private Integer absentClasses;

    private Integer leaveClasses;

    private BigDecimal attendanceRate;

    private BigDecimal leaveRate;
}
