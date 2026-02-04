package com.rosy.main.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 服务记录实体
 */
@Data
@TableName("service_record")
public class ServiceRecord implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 记录ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 活动ID
     */
    private Long activityId;

    /**
     * 用户ID
     */
    private Long userId;

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
     * 证明是否生成：0-未生成 1-已生成
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

    /**
     * 逻辑删除
     */
    @TableLogic
    private Integer isDeleted;
}
