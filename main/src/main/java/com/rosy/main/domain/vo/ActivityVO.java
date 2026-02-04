package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 活动VO
 */
@Data
public class ActivityVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 活动ID
     */
    private Long id;

    /**
     * 活动标题
     */
    private String title;

    /**
     * 活动描述
     */
    private String description;

    /**
     * 活动分类
     */
    private Integer category;

    /**
     * 活动分类描述
     */
    private String categoryDesc;

    /**
     * 活动状态
     */
    private Integer status;

    /**
     * 活动状态描述
     */
    private String statusDesc;

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
    private String location;

    /**
     * 详细地址
     */
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
    private Integer requiredPeople;

    /**
     * 已报名人数
     */
    private Integer registeredPeople;

    /**
     * 已确认人数
     */
    private Integer confirmedPeople;

    /**
     * 联系人姓名
     */
    private String contactName;

    /**
     * 联系人电话
     */
    private String contactPhone;

    /**
     * 报名要求
     */
    private String requirements;

    /**
     * 需要携带的物品
     */
    private String materials;

    /**
     * 活动备注
     */
    private String notes;

    /**
     * 活动封面图
     */
    private String coverImage;

    /**
     * 组织者ID
     */
    private Long organizerId;

    /**
     * 组织者名称
     */
    private String organizerName;

    /**
     * 浏览次数
     */
    private Integer viewCount;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
