package com.rosy.main.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 直播间表
 * </p>
 *
 * @author Rosy
 * @since 2026-01-16
 */
@Data
@TableName("live_room")
public class LiveRoom implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 直播间名称
     */
    private String name;

    /**
     * 直播间简介
     */
    private String description;

    /**
     * 直播间状态：0-未开播，1-直播中，2-已结束
     */
    private Byte status;

    /**
     * 封面图URL
     */
    private String coverUrl;

    /**
     * 推流地址
     */
    private String streamUrl;

    /**
     * 当前讲解商品ID
     */
    private Long currentGoodsId;

    /**
     * 创建者ID
     */
    @TableField(fill = FieldFill.INSERT)
    private Long creatorId;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 开播时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

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
