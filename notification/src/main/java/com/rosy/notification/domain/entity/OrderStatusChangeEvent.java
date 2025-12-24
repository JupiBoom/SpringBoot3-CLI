package com.rosy.notification.domain.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 订单状态变更事件实体
 */
@Data
public class OrderStatusChangeEvent {
    /**
     * 事件ID
     */
    private String eventId;

    /**
     * 订单号
     */
    private String orderId;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 订单金额
     */
    private Double orderAmount;

    /**
     * 订单状态
     */
    private String orderStatus;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 用户手机号
     */
    private String userPhone;

    /**
     * 用户邮箱
     */
    private String userEmail;

    /**
     * 用户微信OpenID
     */
    private String userWechatOpenid;

    /**
     * 事件发生时间
     */
    private LocalDateTime eventTime;

    /**
     * 备注信息
     */
    private String remark;
}