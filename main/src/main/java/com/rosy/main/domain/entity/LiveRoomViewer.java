package com.rosy.main.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 直播间观众记录表
 * </p>
 *
 * @author Rosy
 * @since 2025-01-30
 */
@Data
@TableName("live_room_viewer")
public class LiveRoomViewer implements Serializable {

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
     * 用户ID，可为空（匿名用户）
     */
    private Long userId;

    /**
     * 会话ID，用于识别匿名用户
     */
    private String sessionId;

    /**
     * 进入直播间时间
     */
    private LocalDateTime joinTime;

    /**
     * 离开直播间时间
     */
    private LocalDateTime leaveTime;

    /**
     * 观看总时长（秒）
     */
    private Integer totalDuration;

    /**
     * 是否在线：0-离线，1-在线
     */
    private Byte isOnline;

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