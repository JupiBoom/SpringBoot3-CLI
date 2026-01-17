package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 活动VO类
 *
 * @author Rosy
 * @since 2026-01-17
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
     * 活动名称
     */
    private String name;

    /**
     * 活动描述
     */
    private String description;

    /**
     * 活动时间
     */
    private LocalDateTime activityTime;

    /**
     * 活动地点
     */
    private String location;

    /**
     * 需求人数
     */
    private Integer requiredPeople;

    /**
     * 已报名人数
     */
    private Integer registeredPeople;

    /**
     * 活动分类：1-环保，2-助老，3-教育，4-医疗
     */
    private Byte category;

    /**
     * 活动分类名称
     */
    private String categoryName;

    /**
     * 活动状态：1-招募中，2-进行中，3-已完成
     */
    private Byte status;

    /**
     * 活动状态名称
     */
    private String statusName;

    /**
     * 活动封面图片
     */
    private String coverImage;

    /**
     * 创建者ID
     */
    private Long creatorId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 报名人数百分比
     */
    private Double registrationPercentage;
}