package com.rosy.common.enums;

import lombok.Getter;

@Getter
public enum PostTypeEnum {

    EXPERIENCE((byte) 1, "经验分享"),
    DISCUSSION((byte) 2, "活动讨论");

    private final byte code;
    private final String desc;

    PostTypeEnum(byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static PostTypeEnum getByCode(byte code) {
        for (PostTypeEnum value : values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        return null;
    }
}
