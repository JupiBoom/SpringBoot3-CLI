package com.rosy.main.domain.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 报名创建请求DTO
 */
@Data
public class RegistrationCreateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 活动ID
     */
    @NotNull(message = "活动ID不能为空")
    private Long activityId;

    /**
     * 报名留言
     */
    @Size(max = 500, message = "报名留言长度不能超过500")
    private String message;
}
