package com.rosy.main.domain.dto.leave;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * <p>
 * 请假审批请求对象
 * </p>
 *
 * @author Rosy
 * @since 2025-01-30
 */
@Data
public class LeaveApproveRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 请假ID
     */
    private Long id;

    /**
     * 审批状态：1-已批准，2-已拒绝
     */
    private Byte status;

    /**
     * 审批备注
     */
    private String approveRemark;
}