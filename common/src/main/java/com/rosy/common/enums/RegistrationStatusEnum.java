package com.rosy.common.enums;

public enum RegistrationStatusEnum {
    PENDING("待审核"),
    APPROVED("已通过"),
    REJECTED("已拒绝"),
    CANCELLED("已取消");

    private final String desc;

    RegistrationStatusEnum(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
