package com.rosy.common.enums;

import lombok.Getter;

@Getter
public enum RepairActionTypeEnum {

    ACCEPT(1, "接单"),
    DEPART(2, "出发"),
    ARRIVE(3, "到达"),
    REPAIRING(4, "维修中"),
    COMPLETE(5, "完成");

    private final Integer code;
    private final String desc;

    RepairActionTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getDescByCode(Integer code) {
        for (RepairActionTypeEnum type : values()) {
            if (type.getCode().equals(code)) {
                return type.getDesc();
            }
        }
        return null;
    }
}
