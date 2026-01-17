package com.rosy.volunteer.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ActivityStatusEnum {
    RECRUITING(1, "招募中"),
    IN_PROGRESS(2, "进行中"),
    COMPLETED(3, "已完成");

    private final Integer code;
    private final String desc;

    public static ActivityStatusEnum getByCode(Integer code) {
        for (ActivityStatusEnum status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}
