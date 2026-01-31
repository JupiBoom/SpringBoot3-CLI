package com.rosy.main.enums;

/**
 * 活动状态枚举
 */
public enum ActivityStatusEnum {
    RECRUITING(0, "招募中"),
    IN_PROGRESS(1, "进行中"),
    COMPLETED(2, "已完成"),
    CANCELLED(3, "已取消");

    private final Integer code;
    private final String desc;

    ActivityStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static ActivityStatusEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (ActivityStatusEnum status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}