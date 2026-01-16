package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class HomeworkSubmissionVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long homeworkId;

    private String homeworkTitle;

    private Long studentId;

    private String studentName;

    private String content;

    private String fileUrl;

    private BigDecimal score;

    private String feedback;

    private LocalDateTime submitTime;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
