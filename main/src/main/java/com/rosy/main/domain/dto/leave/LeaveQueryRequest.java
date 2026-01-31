package com.rosy.main.domain.dto.leave;

import com.rosy.common.domain.entity.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * <p>
 * 请假查询请求对象
 * </p>
 *
 * @author Rosy
 * @since 2025-01-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LeaveQueryRequest extends PageRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 学生ID
     */
    private Long studentId;

    /**
     * 课程ID
     */
    private Long courseId;

    /**
     * 审批状态：0-待审批，1-已批准，2-已拒绝
     */
    private Byte status;

    /**
     * 请假开始日期
     */
    private LocalDate startDate;

    /**
     * 请假结束日期
     */
    private LocalDate endDate;
}