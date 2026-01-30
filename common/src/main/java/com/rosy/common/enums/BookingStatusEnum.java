package com.rosy.common.enums;

import lombok.Getter;

@Getter
public enum BookingStatusEnum {

    PENDING_APPROVAL(0, "待审批"),
    APPROVED(1, "已通过"),
    REJECTED(2, "已驳回"),
    CANCELLED(3, "已取消"),
    COMPLETED(4, "已完成");

    private final Integer code;
    private final String text;

    BookingStatusEnum(Integer code, String text) {
        this.code = code;
        this.text = text;
    }

    public static BookingStatusEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (BookingStatusEnum status : BookingStatusEnum.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}
