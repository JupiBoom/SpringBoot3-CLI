package com.rosy.main.domain.dto.attendance;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class AttendanceAddRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "课程ID不能为空")
    private Long courseId;

    @NotNull(message = "学生ID不能为空")
    private Long studentId;

    @NotNull(message = "考勤日期不能为空")
    private LocalDateTime attendanceTime;

    @NotNull(message = "考勤状态不能为空")
    private Integer status;
}
