package com.rosy.main.domain.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 活动更新请求DTO
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
     * 活动标题
     */
    @Size(max = 200, message = "活动标题长度不能超过200")
    private String title;

    /**
     * 活动描述
     */
    private String description;

    /**
     * 活动分类：1-环保 2-助老 3-教育 4-医疗 5-其他
     */
    @Min(value = 1, message = "活动分类不正确")
    @Max(value = 5, message = "活动分类不正确")
    private Integer category;

    /**
     * 活动状态：0-招募中 1-进行中 2-已完成 3-已取消
     */
    @Min(value = 0, message = "活动状态不正确")
    @Max(value = 3, message = "活动状态不正确")
    private Integer status;

    /**
     * 活动开始时间
     */
    private LocalDateTime startTime;

    /**
     * 活动结束时间
     */
    private LocalDateTime endTime;

    /**
     * 报名开始时间
     */
    private LocalDateTime registrationStart;

    /**
     * 报名结束时间
     */
    private LocalDateTime registrationEnd;

    /**
     * 活动地点
     */
    @Size(max = 300, message = "活动地点长度不能超过300")
    private String location;

    /**
     * 详细地址
     */
    @Size(max = 500, message = "详细地址长度不能超过500")
    private String locationDetail;

    /**
     * 经度
     */
    private BigDecimal longitude;

    /**
     * 纬度
     */
    private BigDecimal latitude;

    /**
     * 需求人数
     */
    @Min(value = 1, message = "需求人数至少为1")
    private Integer requiredPeople;

    /**
     * 联系人姓名
     */
    @Size(max = 50, message = "联系人姓名长度不能超过50")
    private String contactName;

    /**
     * 联系人电话
     */
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "联系人电话格式不正确")
    private String contactPhone;

    /**
     * 报名要求
     */
    @Size(max = 500, message = "报名要求长度不能超过500")
    private String requirements;

    /**
     * 需要携带的物品
     */
    @Size(max = 500, message = "携带物品长度不能超过500")
    private String materials;

    /**
     * 活动备注
     */
    private String notes;

    /**
     * 活动封面图
     */
    private String coverImage;
}
