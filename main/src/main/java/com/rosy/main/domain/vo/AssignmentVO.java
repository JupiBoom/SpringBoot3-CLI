package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 作业视图对象
 * </p>
 *
 * @author Rosy
 * @since 2025-01-30
 */
@Data
public class AssignmentVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 作业ID
     */
    private Long id;

    /**
     * 课程ID
     */
    private Long courseId;

    /**
     * 课程名称
     */
    private String courseName;

    /**
     * 作业标题
     */
    private String title;

    /**
     * 作业描述
     */
    private String description;

    /**
     * 作业要求
     */
    private String requirements;

    /**
     * 截止日期
     */
    private LocalDateTime deadline;

    /**
     * 分值
     */
    private Integer score;

    /**
     * 状态：0-未发布，1-已发布，2-已截止
     */
    private Byte status;

    /**
     * 状态描述
     */
    private String statusDesc;

    /**
     * 提交人数
     */
    private Integer submissionCount;

    /**
     * 总人数
     */
    private Integer totalCount;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}