package com.rosy.main.domain.vo;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AttendanceStatisticsVO {
    private Long courseId;
    private Long studentId;
    private LocalDate attendanceDate;
    private Byte status;
}
