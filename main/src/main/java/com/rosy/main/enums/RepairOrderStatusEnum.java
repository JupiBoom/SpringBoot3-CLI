package com.rosy.main.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RepairOrderStatusEnum {

    PENDING(0, "待处理"),
    ASSIGNED(1, "已分配"),
    REPAIRING(2, "维修中"),
    COMPLETED(3, "已完成"),
    CANCELLED(4, "已取消");

    private final Integer code;
    private final String desc;

    public static RepairOrderStatusEnum getByCode(Integer code) {
        for (RepairOrderStatusEnum statusEnum : values()) {
            if (statusEnum.getCode().equals(code)) {
                return statusEnum;
            }
        }
        return null;
    }
}
