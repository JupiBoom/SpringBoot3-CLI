package com.rosy.common.enums;

import lombok.Getter;

@Getter
public enum ActivityCategoryEnum {

    ENVIRONMENTAL((byte) 1, "环保"),
    ELDERLY_CARE((byte) 2, "助老"),
    EDUCATION((byte) 3, "教育"),
    MEDICAL((byte) 4, "医疗"),
    OTHER((byte) 5, "其他");

    private final byte code;
    private final String desc;

    ActivityCategoryEnum(byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ActivityCategoryEnum getByCode(byte code) {
        for (ActivityCategoryEnum value : values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        return null;
    }
}
