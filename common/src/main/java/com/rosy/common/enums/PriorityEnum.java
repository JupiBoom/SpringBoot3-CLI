package com.rosy.common.enums;

import lombok.Getter;

/**
 * 工单优先级枚举
 */
@Getter
public enum PriorityEnum {

    LOW("low", "低", 1),
    MEDIUM("medium", "中", 2),
    HIGH("high", "高", 3),
    URGENT("urgent", "紧急", 4);

    private final String code;
    private final String desc;
    private final int level;

    PriorityEnum(String code, String desc, int level) {
        this.code = code;
        this.desc = desc;
        this.level = level;
    }

    public static PriorityEnum getByCode(String code) {
        for (PriorityEnum priority : values()) {
            if (priority.getCode().equals(code)) {
                return priority;
            }
        }
        return MEDIUM;
    }
}
