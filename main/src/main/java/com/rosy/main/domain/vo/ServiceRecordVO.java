package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 服务记录VO
 */
@Data
public class ServiceRecordVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 记录ID
     */
    private Long id;

    /**
     * 活动ID
     */
    private Long activityId;

    /**
     * 活动标题
     */
    private String activityTitle;

    /**
     * 活动分类
     */
    private Integer activityCategory;

    /**
     * 活动分类描述
     */
    private String activityCategoryDesc;

    /**
     * 活动地点
     */
    private String activityLocation;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户姓名
     */
    private String userName;

    /**
     * 报名ID
     */
    private Long registrationId;

    /**
     * 服务日期
     */
    private LocalDate serviceDate;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 服务时长（小时）
     */
    private BigDecimal durationHours;

    /**
     * 服务内容
     */
    private String serviceContent;

    /**
     * 服务表现：1-5星评价
     */
    private Integer performance;

    /**
     * 组织者评价
     */
    private String organizerComment;

    /**
     * 志愿者评价
     */
    private String volunteerComment;

    /**
     * 服务证明编号
     */
    private String certificateNo;

    /**
     * 服务证明PDF链接
     */
    private String certificateUrl;

    /**
     * 证明是否生成
     */
    private Integer certificateGenerated;

    /**
     * 证明生成时间
     */
    private LocalDateTime certificateGenerateTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
