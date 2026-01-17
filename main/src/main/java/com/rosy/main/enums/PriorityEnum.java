package com.rosy.main.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PriorityEnum {

    URGENT(1, "紧急"),
    NORMAL(2, "普通"),
    LOW(3, "低");

    private final Integer code;
    private final String desc;

    public static PriorityEnum getByCode(Integer code) {
        for (PriorityEnum priorityEnum : values()) {
            if (priorityEnum.getCode().equals(code)) {
                return priorityEnum;
            }
        }
        return null;
    }
}
