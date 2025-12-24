package com.rosy.main.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户通知偏好设置实体
 *
 * @author rosy
 */
@Data
@TableName("user_notification_preference")
public class UserNotificationPreference {

    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 是否启用短信通知
     */
    private Integer enableSms;

    /**
     * 是否启用微信通知
     */
    private Integer enableWeChat;

    /**
     * 是否启用邮件通知
     */
    private Integer enableEmail;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 版本
     */
    @Version
    private Integer version;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDeleted;
}