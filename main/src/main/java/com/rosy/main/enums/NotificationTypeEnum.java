package com.rosy.main.enums;

/**
 * 通知类型枚举
 */
public enum NotificationTypeEnum {
    SYSTEM(0, "系统通知"),
    ACTIVITY_REMINDER(1, "活动提醒"),
    ENROLLMENT_AUDIT(2, "报名审核"),
    SERVICE_RATING(3, "服务评价");

    private final Integer code;
    private final String desc;

    NotificationTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static NotificationTypeEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (NotificationTypeEnum type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}