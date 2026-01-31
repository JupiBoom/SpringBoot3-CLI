package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 作业提交视图对象
 * </p>
 *
 * @author Rosy
 * @since 2025-01-30
 */
@Data
public class AssignmentSubmissionVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 提交ID
     */
    private Long id;

    /**
     * 作业ID
     */
    private Long assignmentId;

    /**
     * 作业标题
     */
    private String assignmentTitle;

    /**
     * 课程ID
     */
    private Long courseId;

    /**
     * 课程名称
     */
    private String courseName;

    /**
     * 学生ID
     */
    private Long studentId;

    /**
     * 学生姓名
     */
    private String studentName;

    /**
     * 学生学号
     */
    private String studentNo;

    /**
     * 提交内容
     */
    private String content;

    /**
     * 附件路径
     */
    private String attachmentPath;

    /**
     * 提交时间
     */
    private LocalDateTime submitTime;

    /**
     * 得分
     */
    private Integer score;

    /**
     * 评语
     */
    private String comment;

    /**
     * 评分教师ID
     */
    private Long graderId;

    /**
     * 评分教师姓名
     */
    private String graderName;

    /**
     * 评分时间
     */
    private LocalDateTime gradeTime;

    /**
     * 状态：0-未提交，1-已提交，2-已评分
     */
    private Byte status;

    /**
     * 状态描述
     */
    private String statusDesc;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}