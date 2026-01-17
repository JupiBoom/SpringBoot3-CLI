package com.rosy.main.domain.dto.activity;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 活动更新请求DTO
 *
 * @author Rosy
 * @since 2026-01-17
 */
@Data
public class ActivityUpdateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 活动ID
     */
    @NotNull(message = "活动ID不能为空")
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
     * 活动时间
     */
    private LocalDateTime activityTime;

    /**
     * 活动地点
     */
    @Size(max = 200, message = "活动地点长度不能超过200个字符")
    private String location;

    /**
     * 需求人数
     */
    @Min(value = 1, message = "需求人数不能小于1")
    @Max(value = 10000, message = "需求人数不能超过10000")
    private Integer requiredPeople;

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
     * 活动封面图片
     */
    @Size(max = 500, message = "封面图片地址长度不能超过500个字符")
    private String coverImage;
}