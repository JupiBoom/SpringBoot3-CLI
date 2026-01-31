package com.rosy.common.enums;

import lombok.Getter;

@Getter
public enum NotificationTypeEnum {

    ACTIVITY_REMINDER(1, "活动提醒"),
    REVIEW_RESULT(2, "审核结果"),
    SYSTEM_NOTICE(3, "系统通知");

    private final Integer value;
    private final String text;

    NotificationTypeEnum(Integer value, String text) {
        this.value = value;
        this.text = text;
    }

    public static String getTextByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (NotificationTypeEnum type : values()) {
            if (type.getValue().equals(value)) {
                return type.getText();
            }
        }
        return null;
    }
}
