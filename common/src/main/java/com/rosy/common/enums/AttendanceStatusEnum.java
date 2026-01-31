package com.rosy.common.enums;

import lombok.Getter;

@Getter
public enum AttendanceStatusEnum {

    NORMAL((byte) 0, "正常"),
    LATE((byte) 1, "迟到"),
    ABSENT((byte) 2, "缺勤"),
    LEAVE((byte) 3, "请假");

    private final Byte code;
    private final String desc;

    AttendanceStatusEnum(Byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}