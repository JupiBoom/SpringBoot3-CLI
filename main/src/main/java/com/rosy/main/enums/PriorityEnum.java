package com.rosy.main.enums;

/**
 * 优先级枚举
 *
 * @author Rosy
 * @since 2025-01-31
 */
public enum PriorityEnum {
    LOW(1, "低"),
    MEDIUM(2, "中"),
    HIGH(3, "高"),
    URGENT(4, "紧急");

    private final int code;
    private final String description;

    PriorityEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static PriorityEnum getByCode(int code) {
        for (PriorityEnum priority : values()) {
            if (priority.getCode() == code) {
                return priority;
            }
        }
        return null;
    }
}