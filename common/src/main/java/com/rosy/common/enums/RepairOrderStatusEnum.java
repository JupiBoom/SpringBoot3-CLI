package com.rosy.common.enums;

import lombok.Getter;

@Getter
public enum RepairOrderStatusEnum {

    PENDING(0, "待处理"),
    ASSIGNED(1, "已分配"),
    REPAIRING(2, "维修中"),
    CONFIRMING(3, "待确认"),
    COMPLETED(4, "已完成"),
    CANCELLED(5, "已取消");

    private final Integer code;
    private final String desc;

    RepairOrderStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getDescByCode(Integer code) {
        for (RepairOrderStatusEnum status : values()) {
            if (status.getCode().equals(code)) {
                return status.getDesc();
            }
        }
        return null;
    }
}
