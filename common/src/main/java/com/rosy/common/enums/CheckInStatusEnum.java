package com.rosy.common.enums;

import lombok.Getter;

@Getter
public enum CheckInStatusEnum {

    NOT_CHECKED_IN(0, "未签到"),
    CHECKED_IN(1, "已签到"),
    ABSENT(2, "缺席");

    private final Integer code;
    private final String text;

    CheckInStatusEnum(Integer code, String text) {
        this.code = code;
        this.text = text;
    }

    public static CheckInStatusEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (CheckInStatusEnum status : CheckInStatusEnum.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}
