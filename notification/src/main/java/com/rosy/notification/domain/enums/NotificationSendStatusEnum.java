package com.rosy.notification.domain.enums;

import lombok.Getter;

/**
 * 通知发送状态枚举
 */
@Getter
public enum NotificationSendStatusEnum {
    /**
     * 待发送
     */
    PENDING("pending", "待发送"),

    /**
     * 发送中
     */
    SENDING("sending", "发送中"),

    /**
     * 发送成功
     */
    SUCCESS("success", "发送成功"),

    /**
     * 发送失败
     */
    FAIL("fail", "发送失败"),

    /**
     * 已取消
     */
    CANCELLED("cancelled", "已取消");

    private final String code;
    private final String desc;

    NotificationSendStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 根据code获取枚举
     */
    public static NotificationSendStatusEnum getByCode(String code) {
        for (NotificationSendStatusEnum status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}