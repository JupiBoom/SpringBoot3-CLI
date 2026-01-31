package com.rosy.main.domain.dto.attendance;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class AttendanceUpdateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "ID不能为空")
    private Long id;

    private Long courseId;

    private Long studentId;

    private LocalDateTime attendanceTime;

    private Integer status;

    private String remark;
}
