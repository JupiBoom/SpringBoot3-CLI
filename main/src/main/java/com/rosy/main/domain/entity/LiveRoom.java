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
     * 直播间标题
     */
    private String title;

    /**
     * 直播间封面图URL
     */
    private String coverImage;

    /**
     * 直播间状态：0-未开播，1-直播中，2-已结束，3-暂停
     */
    private Byte status;

    /**
     * 主播ID，关联用户表
     */
    private Long anchorId;

    /**
     * 主播名称
     */
    private String anchorName;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 当前讲解商品ID
     */
    private Long currentProductId;

    /**
     * 预计直播时长（分钟）
     */
    private Integer expectedDuration;

    /**
     * 直播间描述
     */
    private String description;

    /**
     * 观看人数
     */
    private Integer viewerCount;

    /**
     * 最高在线人数
     */
    private Integer peakViewerCount;

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
     * 更新者ID
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