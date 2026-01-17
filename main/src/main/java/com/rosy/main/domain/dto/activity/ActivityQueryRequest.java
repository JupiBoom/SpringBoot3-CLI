package com.rosy.main.domain.dto.activity;

import com.rosy.common.domain.entity.PageRequest;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 活动查询请求DTO
 *
 * @author Rosy
 * @since 2026-01-17
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ActivityQueryRequest extends PageRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 活动ID
     */
    @Positive(message = "活动ID必须为正整数")
    private Long id;

    /**
     * 活动名称
     */
    @Size(max = 200, message = "活动名称长度不能超过200个字符")
    private String name;

    /**
     * 活动描述
     */
    @Size(max = 2000, message = "活动描述长度不能超过2000个字符")
    private String description;

    /**
     * 活动地点
     */
    @Size(max = 200, message = "活动地点长度不能超过200个字符")
    private String location;

    /**
     * 活动分类：1-环保，2-助老，3-教育，4-医疗
     */
    @Min(value = 1, message = "活动分类值不合法")
    @Max(value = 4, message = "活动分类值不合法")
    private Byte category;

    /**
     * 活动状态：1-招募中，2-进行中，3-已完成
     */
    @Min(value = 1, message = "活动状态值不合法")
    @Max(value = 3, message = "活动状态值不合法")
    private Byte status;

    /**
     * 创建者ID
     */
    @Positive(message = "创建者ID必须为正整数")
    private Long creatorId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 活动时间开始
     */
    private LocalDateTime activityTimeStart;

    /**
     * 活动时间结束
     */
    private LocalDateTime activityTimeEnd;
}