package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 观众留存曲线数据点视图对象
 * </p>
 *
 * @author Rosy
 * @since 2025-01-30
 */
@Data
public class ViewerRetentionPointVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 时间点（分钟）
     */
    private Integer timePoint;

    /**
     * 在线观众人数
     */
    private Integer onlineViewers;

    /**
     * 留存率（相对于初始观众数的百分比）
     */
    private Double retentionRate;

    /**
     * 留存率百分比字符串
     */
    private String retentionRatePercent;

    /**
     * 时间戳
     */
    private LocalDateTime timestamp;
}