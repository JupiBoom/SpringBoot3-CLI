package com.rosy.main.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReservationStatusEnum {

    PENDING(0, "待审批"),
    APPROVED(1, "已通过"),
    REJECTED(2, "已驳回"),
    CANCELLED(3, "已取消");

    private final Integer code;
    private final String desc;

    public static String getDescByCode(Integer code) {
        for (ReservationStatusEnum status : values()) {
            if (status.getCode().equals(code)) {
                return status.getDesc();
            }
        }
        return "未知状态";
    }
}
