package com.rosy.main.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 直播间数据统计表
 * </p>
 *
 * @author Rosy
 * @since 2025-01-30
 */
@Data
@TableName("live_room_stats")
public class LiveRoomStats implements Serializable {

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
     * 统计日期
     */
    private LocalDate statsDate;

    /**
     * 总观众人数
     */
    private Integer totalViewers;

    /**
     * 峰值观众人数
     */
    private Integer peakViewers;

    /**
     * 平均观看时长（秒）
     */
    private Integer avgViewTime;

    /**
     * 总订单数
     */
    private Integer totalOrders;

    /**
     * 总销售额
     */
    private BigDecimal totalSales;

    /**
     * 转化率（订单数/观众数）
     */
    private BigDecimal conversionRate;

    /**
     * 创建者ID，关联用户表
     */
    @TableField(fill = FieldFill.INSERT)
    private Long creatorId;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新者ID，关联用户表
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updaterId;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 乐观锁版本号
     */
    @Version
    private Byte version;

    /**
     * 是否删除：0-未删除，1-已删除
     */
    @TableLogic
    private Byte isDeleted;
}