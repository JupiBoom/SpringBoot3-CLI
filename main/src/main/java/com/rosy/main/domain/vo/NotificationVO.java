package com.rosy.main.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 通知VO类
 */
@Data
public class NotificationVO {

    /**
     * ID
     */
    private Long id;

    /**
     * 接收通知的用户ID
     */
    private Long userId;

    /**
     * 通知标题
     */
    private String title;

    /**
     * 通知内容
     */
    private String content;

    /**
     * 通知类型：0-审批结果通知，1-会议开始前通知
     */
    private Integer type;

    /**
     * 通知类型描述
     */
    private String typeDesc;

    /**
     * 关联ID（预约ID等）
     */
    private Long relatedId;

    /**
     * 是否已读：0-未读，1-已读
     */
    private Integer isRead;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 阅读时间
     */
    private LocalDateTime readTime;
}