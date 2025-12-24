package com.rosy.notification.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户通知偏好设置实体
 */
@Data
@TableName("user_notification_preference")
public class UserNotificationPreference {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 通知类型（短信/微信/邮件）
     */
    private String notificationType;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 通知渠道（手机号/邮箱/微信OpenID）
     */
    private String channel;

    /**
     * 备注信息
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}