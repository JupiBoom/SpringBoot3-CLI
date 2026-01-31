package com.rosy.main.enums;

/**
 * 报名状态枚举
 */
public enum EnrollmentStatusEnum {
    PENDING(0, "待审核"),
    APPROVED(1, "审核通过"),
    REJECTED(2, "审核拒绝");

    private final Integer code;
    private final String desc;

    EnrollmentStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static EnrollmentStatusEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (EnrollmentStatusEnum status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}