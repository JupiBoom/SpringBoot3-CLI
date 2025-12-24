package com.rosy.notification.domain.enums;

import lombok.Getter;

/**
 * 通知类型枚举
 */
@Getter
public enum NotificationTypeEnum {
    /**
     * 短信通知
     */
    SMS("sms", "短信通知"),

    /**
     * 微信通知
     */
    WECHAT("wechat", "微信通知"),

    /**
     * 邮件通知
     */
    EMAIL("email", "邮件通知");

    private final String code;
    private final String desc;

    NotificationTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 根据code获取枚举
     */
    public static NotificationTypeEnum getByCode(String code) {
        for (NotificationTypeEnum type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}