package com.rosy.main.domain.dto;

import com.rosy.common.domain.entity.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * 活动查询请求DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ActivityQueryRequest extends PageRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 搜索关键词（标题/描述）
     */
    private String keyword;

    /**
     * 活动分类
     */
    private Integer category;

    /**
     * 活动状态
     */
    private Integer status;

    /**
     * 组织者ID
     */
    private Long organizerId;

    /**
     * 活动日期（开始）
     */
    private LocalDate startDate;

    /**
     * 活动日期（结束）
     */
    private LocalDate endDate;

    /**
     * 地点
     */
    private String location;
}
