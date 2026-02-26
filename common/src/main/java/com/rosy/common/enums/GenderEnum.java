package com.rosy.common.enums;

import lombok.Getter;

@Getter
public enum GenderEnum {

    UNKNOWN((byte) 0, "未知"),
    MALE((byte) 1, "男"),
    FEMALE((byte) 2, "女");

    private final byte code;
    private final String desc;

    GenderEnum(byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static GenderEnum getByCode(byte code) {
        for (GenderEnum value : values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        return null;
    }
}
