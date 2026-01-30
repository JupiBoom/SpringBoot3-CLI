package com.rosy.main.domain.dto.booking;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class ApprovalRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "预约ID不能为空")
    private Long bookingId;

    @NotNull(message = "审批结果不能为空")
    private Byte approvalResult;

    private String approvalComment;
}
