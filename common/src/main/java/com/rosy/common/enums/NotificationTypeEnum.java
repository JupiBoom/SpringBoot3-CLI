package com.rosy.common.enums;

public enum NotificationTypeEnum {
    ACTIVITY("活动通知"),
    SYSTEM("系统消息");

    private final String desc;

    NotificationTypeEnum(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
