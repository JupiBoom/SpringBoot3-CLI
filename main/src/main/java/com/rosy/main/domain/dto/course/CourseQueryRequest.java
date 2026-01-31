package com.rosy.main.domain.dto.course;

import com.rosy.common.domain.entity.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
public class CourseQueryRequest extends PageRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String courseName;

    private String courseCode;

    private BigDecimal credits;

    private Long teacherId;

    private String classroom;

    private String schedule;
}
