package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class LeaveRequestVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long studentId;

    private String studentName;

    private Long courseId;

    private String courseName;

    private LocalDate startDate;

    private LocalDate endDate;

    private String reason;

    private Byte status;

    private Long approverId;

    private String approverName;

    private LocalDateTime approveTime;

    private String approveRemark;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
