package com.rosy.main.domain.dto.assignment;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 作业评分请求对象
 * </p>
 *
 * @author Rosy
 * @since 2025-01-30
 */
@Data
public class SubmissionGradeRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 提交ID
     */
    private Long id;

    /**
     * 得分
     */
    private Integer score;

    /**
     * 评语
     */
    private String comment;
}