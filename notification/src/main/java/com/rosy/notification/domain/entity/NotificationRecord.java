package com.rosy.notification.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 通知记录实体
 */
@Data
@TableName("notification_record")
public class NotificationRecord {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 通知ID
     */
    private String notificationId;

    /**
     * 订单号
     */
    private String orderId;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 通知类型
     */
    private String notificationType;

    /**
     * 通知模板ID
     */
    private String templateId;

    /**
     * 通知内容
     */
    private String content;

    /**
     * 目标地址（手机号/邮箱/微信OpenID）
     */
    private String targetAddress;

    /**
     * 发送状态
     */
    private String sendStatus;

    /**
     * 发送次数
     */
    private Integer sendCount;

    /**
     * 最后一次发送时间
     */
    private LocalDateTime lastSendTime;

    /**
     * 首次发送时间
     */
    private LocalDateTime firstSendTime;

    /**
     * 成功时间
     */
    private LocalDateTime successTime;

    /**
     * 错误信息
     */
    private String errorMsg;

    /**
     * 重试次数
     */
    private Integer retryCount;

    /**
     * 下次重试时间
     */
    private LocalDateTime nextRetryTime;

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