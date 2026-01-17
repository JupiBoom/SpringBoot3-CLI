package com.rosy.main.enums;

import lombok.Getter;

/**
 * 活动状态枚举
 */
@Getter
public enum ActivityStatusEnum {

    RECRUITING(1, "招募中"),
    IN_PROGRESS(2, "进行中"),
    COMPLETED(3, "已完成");

    private final Integer code;
    private final String desc;

    ActivityStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ActivityStatusEnum fromCode(Integer code) {
        for (ActivityStatusEnum activityStatus : values()) {
            if (activityStatus.getCode().equals(code)) {
                return activityStatus;
            }
        }
        return null;
    }
}
