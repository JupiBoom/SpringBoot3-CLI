package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

@Data
public class AttendanceVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long courseId;

    private String courseName;

    private Long studentId;

    private String studentName;

    private LocalDate attendanceDate;

    private Byte status;

    private String remark;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
