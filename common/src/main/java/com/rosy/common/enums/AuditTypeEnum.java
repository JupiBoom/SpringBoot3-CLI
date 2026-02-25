package com.rosy.common.enums;

public enum AuditTypeEnum {
    AUTO("自动审核"),
    MANUAL("人工审核");

    private final String desc;

    AuditTypeEnum(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
