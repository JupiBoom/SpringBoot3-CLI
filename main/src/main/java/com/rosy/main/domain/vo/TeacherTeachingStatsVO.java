package com.rosy.main.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TeacherTeachingStatsVO {

    private Long teacherId;

    private String teacherName;

    private Integer totalCourses;

    private Integer activeCourses;

    private Integer totalStudents;

    private Integer totalClasses;

    private BigDecimal averageAttendanceRate;
}
