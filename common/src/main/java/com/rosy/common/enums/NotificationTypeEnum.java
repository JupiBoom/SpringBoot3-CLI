package com.rosy.common.enums;

import lombok.Getter;

@Getter
public enum NotificationTypeEnum {

    ACTIVITY_REMINDER((byte) 1, "活动提醒"),
    REGISTRATION_AUDIT((byte) 2, "报名审核"),
    SYSTEM((byte) 3, "系统通知");

    private final byte code;
    private final String desc;

    NotificationTypeEnum(byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static NotificationTypeEnum getByCode(byte code) {
        for (NotificationTypeEnum value : values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        return null;
    }
}
