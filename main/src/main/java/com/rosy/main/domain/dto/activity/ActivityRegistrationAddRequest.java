package com.rosy.main.domain.dto.activity;

import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 活动报名请求
 */
@Data
public class ActivityRegistrationAddRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 活动ID
     */
    @NotNull(message = "活动ID不能为空")
    private Long activityId;

    /**
     * 志愿者ID
     */
    @NotNull(message = "志愿者ID不能为空")
    private Long volunteerId;

    /**
     * 报名备注
     */
    private String remark;
}
