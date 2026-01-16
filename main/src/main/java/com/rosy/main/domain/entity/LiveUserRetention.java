package com.rosy.main.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 观众留存记录表
 * </p>
 *
 * @author Rosy
 * @since 2026-01-16
 */
@Data
@TableName("live_user_retention")
public class LiveUserRetention implements Serializable {

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
     * 观众进入时间
     */
    private LocalDateTime enterTime;

    /**
     * 观众离开时间
     */
    private LocalDateTime leaveTime;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 停留时长（秒）
     */
    private Integer duration;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
