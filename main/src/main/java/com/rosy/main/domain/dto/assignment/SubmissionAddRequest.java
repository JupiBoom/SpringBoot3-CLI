package com.rosy.main.domain.dto.assignment;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 作业提交添加请求对象
 * </p>
 *
 * @author Rosy
 * @since 2025-01-30
 */
@Data
public class SubmissionAddRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 作业ID
     */
    private Long assignmentId;

    /**
     * 学生ID
     */
    private Long studentId;

    /**
     * 提交内容
     */
    private String content;

    /**
     * 附件路径
     */
    private String attachmentPath;
}