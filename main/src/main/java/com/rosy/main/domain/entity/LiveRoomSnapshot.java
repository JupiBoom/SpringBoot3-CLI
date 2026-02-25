package com.rosy.main.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 直播间数据快照表
 * </p>
 *
 * @author Rosy
 * @since 2025-02-25
 */
@Data
@TableName("live_room_snapshot")
public class LiveRoomSnapshot implements Serializable {

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
    private Long roomId;

    /**
     * 快照时间
     */
    private LocalDateTime snapshotTime;

    /**
     * 当前观众数
     */
    private Integer viewerCount;

    /**
     * 累计观看人数
     */
    private Integer totalViewers;

    /**
     * 点赞数
     */
    private Integer likeCount;

    /**
     * 订单数
     */
    private Integer orderCount;

    /**
     * 销售额
     */
    private BigDecimal salesAmount;

    /**
     * 新增订单数
     */
    private Integer newOrders;

    /**
     * 新增销售额
     */
    private BigDecimal newSales;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
