package com.rosy.main.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 直播间观众数据统计表
 * </p>
 *
 * @author Rosy
 * @since 2025-02-25
 */
@Data
@TableName("live_viewer_stats")
public class LiveViewerStats implements Serializable {

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
     * 用户ID（未登录为NULL）
     */
    private Long userId;

    /**
     * 进入时间
     */
    private LocalDateTime enterTime;

    /**
     * 离开时间
     */
    private LocalDateTime leaveTime;

    /**
     * 停留时长（秒）
     */
    private Integer stayDuration;

    /**
     * 是否登录用户：0-否，1-是
     */
    private Byte isLogin;

    /**
     * IP地址
     */
    private String ipAddress;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
