package com.rosy.common.enums;

import lombok.Getter;

@Getter
public enum RepairStaffStatusEnum {

    OFFLINE(0, "离线"),
    ONLINE(1, "在线"),
    BUSY(2, "忙碌");

    private final Integer code;
    private final String desc;

    RepairStaffStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getDescByCode(Integer code) {
        for (RepairStaffStatusEnum status : values()) {
            if (status.getCode().equals(code)) {
                return status.getDesc();
            }
        }
        return null;
    }
}
