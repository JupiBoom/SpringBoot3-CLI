package com.rosy.main.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApprovalStatusEnum {

    APPROVED(1, "通过"),
    REJECTED(2, "驳回");

    private final Integer code;
    private final String desc;

    public static String getDescByCode(Integer code) {
        for (ApprovalStatusEnum status : values()) {
            if (status.getCode().equals(code)) {
                return status.getDesc();
            }
        }
        return "未知状态";
    }
}
