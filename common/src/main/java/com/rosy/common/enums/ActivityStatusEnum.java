package com.rosy.common.enums;

import lombok.Getter;

@Getter
public enum ActivityStatusEnum {

    RECRUITING((byte) 1, "招募中"),
    IN_PROGRESS((byte) 2, "进行中"),
    COMPLETED((byte) 3, "已完成"),
    CANCELLED((byte) 4, "已取消");

    private final byte code;
    private final String desc;

    ActivityStatusEnum(byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ActivityStatusEnum getByCode(byte code) {
        for (ActivityStatusEnum value : values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        return null;
    }
}
