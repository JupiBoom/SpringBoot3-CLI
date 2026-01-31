package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 请假视图对象
 * </p>
 *
 * @author Rosy
 * @since 2025-01-30
 */
@Data
public class LeaveRequestVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 请假ID
     */
    private Long id;

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
     * 课程ID
     */
    private Long courseId;

    /**
     * 课程名称
     */
    private String courseName;

    /**
     * 请假开始日期
     */
    private LocalDate startDate;

    /**
     * 请假结束日期
     */
    private LocalDate endDate;

    /**
     * 请假原因
     */
    private String reason;

    /**
     * 审批状态：0-待审批，1-已批准，2-已拒绝
     */
    private Byte status;

    /**
     * 审批状态描述
     */
    private String statusDesc;

    /**
     * 审批人ID
     */
    private Long approverId;

    /**
     * 审批人姓名
     */
    private String approverName;

    /**
     * 审批时间
     */
    private LocalDateTime approveTime;

    /**
     * 审批备注
     */
    private String approveRemark;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}