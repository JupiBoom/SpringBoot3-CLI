package com.rosy.main.domain.dto;

import com.rosy.common.domain.entity.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * 报名查询请求DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RegistrationQueryRequest extends PageRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 活动ID
     */
    private Long activityId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 报名状态
     */
    private Integer status;

    /**
     * 是否需要发送提醒（用于定时任务查询）
     */
    private Boolean needReminder;
}
