package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CourseVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private BigDecimal credit;

    private Long teacherId;

    private String teacherName;

    private String classroom;

    private Integer maxStudents;

    private Integer currentStudents;

    private Byte status;

    private Long creatorId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
