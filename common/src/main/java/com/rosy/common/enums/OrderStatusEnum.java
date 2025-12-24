package com.rosy.common.enums;

import lombok.Getter;

/**
 * 订单状态枚举
 *
 * @author rosy
 */
@Getter
public enum OrderStatusEnum {

    /**
     * 待支付
     */
    PENDING_PAYMENT(1, "待支付"),

    /**
     * 支付成功
     */
    PAID(2, "支付成功"),

    /**
     * 已发货
     */
    SHIPPED(3, "已发货"),

    /**
     * 已签收
     */
    RECEIVED(4, "已签收"),

    /**
     * 已取消
     */
    CANCELED(5, "已取消"),

    /**
     * 已完成
     */
    COMPLETED(6, "已完成");

    private final Integer code;
    private final String desc;

    OrderStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 根据编码获取枚举
     */
    public static OrderStatusEnum getByCode(Integer code) {
        for (OrderStatusEnum status : OrderStatusEnum.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}