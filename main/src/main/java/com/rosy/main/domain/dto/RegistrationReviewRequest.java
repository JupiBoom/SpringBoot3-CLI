package com.rosy.main.domain.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 报名审核请求DTO
 */
@Data
public class RegistrationReviewRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 报名ID
     */
    @NotNull(message = "报名ID不能为空")
    private Long id;

    /**
     * 审核结果：1-通过 2-拒绝
     */
    @NotNull(message = "审核结果不能为空")
    private Integer status;

    /**
     * 拒绝原因
     */
    @Size(max = 500, message = "拒绝原因长度不能超过500")
    private String rejectReason;
}
