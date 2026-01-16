package com.rosy.main.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 直播数据表
 * </p>
 *
 * @author Rosy
 * @since 2026-01-16
 */
@Data
@TableName("live_data")
public class LiveData implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 直播间ID
     */
    private Long liveRoomId;

    /**
     * 统计时间点
     */
    private LocalDateTime statTime;

    /**
     * 当前在线人数
     */
    private Integer currentViewers;

    /**
     * 累计观看人数
     */
    private Integer totalViewers;

    /**
     * 新增观众数
     */
    private Integer newViewers;

    /**
     * 互动次数（评论、点赞等）
     */
    private Integer interactionCount;

    /**
     * 商品点击次数
     */
    private Integer productClickCount;

    /**
     * 下单数
     */
    private Integer orderCount;

    /**
     * 成交金额
     */
    private BigDecimal salesAmount;

    /**
     * 观众留存率
     */
    private BigDecimal retentionRate;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}