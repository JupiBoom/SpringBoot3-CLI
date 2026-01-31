package com.rosy.main.domain.dto.leave;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class LeaveApplicationUpdateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "ID不能为空")
    private Long id;

    private Long courseId;

    private Long studentId;

    private Integer leaveType;

    private Integer status;

    @Size(max = 500, message = "请假原因长度不能超过500个字符")
    private String reason;

    @Size(max = 200, message = "审批意见长度不能超过200个字符")
    private String approveRemark;
}
