package com.rosy.common.enums;

import lombok.Getter;

@Getter
public enum RepairPriorityEnum {

    LOW(1, "低"),
    MEDIUM(2, "中"),
    HIGH(3, "高"),
    URGENT(4, "紧急");

    private final Integer code;
    private final String desc;

    RepairPriorityEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getDescByCode(Integer code) {
        for (RepairPriorityEnum priority : values()) {
            if (priority.getCode().equals(code)) {
                return priority.getDesc();
            }
        }
        return null;
    }
}
