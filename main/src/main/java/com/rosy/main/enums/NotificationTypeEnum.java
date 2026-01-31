package com.rosy.main.enums;

/**
 * 通知类型枚举
 *
 * @author Rosy
 * @since 2025-01-31
 */
public enum NotificationTypeEnum {
    SYSTEM(0, "系统通知"),
    REPAIR_ORDER(1, "工单通知"),
    EVALUATION(2, "评价通知");

    private final int code;
    private final String description;

    NotificationTypeEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static NotificationTypeEnum getByCode(int code) {
        for (NotificationTypeEnum type : values()) {
            if (type.getCode() == code) {
                return type;
            }
        }
        return null;
    }
}