package com.rosy.common.enums;

import lombok.Getter;

@Getter
public enum LeaveTypeEnum {

    PERSONAL((byte) 1, "事假"),
    SICK((byte) 2, "病假"),
    OTHER((byte) 3, "其他");

    private final Byte code;
    private final String desc;

    LeaveTypeEnum(Byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}