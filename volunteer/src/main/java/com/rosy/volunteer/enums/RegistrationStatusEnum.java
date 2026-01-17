package com.rosy.volunteer.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RegistrationStatusEnum {
    PENDING(1, "待审核"),
    APPROVED(2, "已通过"),
    REJECTED(3, "已拒绝"),
    CANCELLED(4, "已取消");

    private final Integer code;
    private final String desc;

    public static RegistrationStatusEnum getByCode(Integer code) {
        for (RegistrationStatusEnum status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}
