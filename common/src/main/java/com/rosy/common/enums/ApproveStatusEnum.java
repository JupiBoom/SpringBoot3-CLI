package com.rosy.common.enums;

import lombok.Getter;

@Getter
public enum ApproveStatusEnum {

    PENDING((byte) 0, "待审批"),
    APPROVED((byte) 1, "已通过"),
    REJECTED((byte) 2, "已拒绝");

    private final Byte code;
    private final String desc;

    ApproveStatusEnum(Byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}