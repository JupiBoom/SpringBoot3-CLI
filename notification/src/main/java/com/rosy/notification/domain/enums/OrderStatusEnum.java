package com.rosy.notification.domain.enums;

import lombok.Getter;

/**
 * 订单状态枚举
 */
@Getter
public enum OrderStatusEnum {
    /**
     * 待支付
     */
    WAIT_PAY("wait_pay", "待支付"),

    /**
     * 支付成功
     */
    PAY_SUCCESS("pay_success", "支付成功"),

    /**
     * 已发货
     */
    SHIPPED("shipped", "已发货"),

    /**
     * 已签收
     */
    SIGNED("signed", "已签收"),

    /**
     * 已取消
     */
    CANCELLED("cancelled", "已取消");

    private final String code;
    private final String desc;

    OrderStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 根据code获取枚举
     */
    public static OrderStatusEnum getByCode(String code) {
        for (OrderStatusEnum status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}