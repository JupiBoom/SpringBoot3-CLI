package com.rosy.common.enums;

public enum ActivityStatusEnum {
    RECRUITING("招募中"),
    ONGOING("进行中"),
    COMPLETED("已完成"),
    CANCELLED("已取消");

    private final String desc;

    ActivityStatusEnum(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
