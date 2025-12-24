package com.rosy.notification.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 通知模板实体
 */
@Data
@TableName("notification_template")
public class NotificationTemplate {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 模板ID
     */
    private String templateId;

    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 通知类型
     */
    private String notificationType;

    /**
     * 订单状态
     */
    private String orderStatus;

    /**
     * 模板内容
     */
    private String templateContent;

    /**
     * 模板变量（JSON格式）
     */
    private String templateVariables;

    /**
     * 模板描述
     */
    private String description;

    /**
     * 是否启用
     */
    private Boolean enabled;

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