package com.rosy.common.enums;

import lombok.Getter;

@Getter
public enum RegistrationStatusEnum {

    PENDING((byte) 0, "待审核"),
    APPROVED((byte) 1, "已通过"),
    REJECTED((byte) 2, "已拒绝"),
    CANCELLED((byte) 3, "已取消");

    private final byte code;
    private final String desc;

    RegistrationStatusEnum(byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static RegistrationStatusEnum getByCode(byte code) {
        for (RegistrationStatusEnum value : values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        return null;
    }
}
