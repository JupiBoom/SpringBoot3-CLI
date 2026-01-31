package com.rosy.main.domain.dto.leave;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class LeaveApplicationAddRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "课程ID不能为空")
    private Long courseId;

    @NotNull(message = "学生ID不能为空")
    private Long studentId;

    @NotNull(message = "请假类型不能为空")
    private Integer leaveType;

    @NotNull(message = "开始时间不能为空")
    private LocalDateTime startTime;

    @NotNull(message = "结束时间不能为空")
    private LocalDateTime endTime;

    @NotBlank(message = "请假原因不能为空")
    @Size(max = 500, message = "请假原因长度不能超过500个字符")
    private String reason;
}
