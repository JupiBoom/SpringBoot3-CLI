package com.rosy.common.enums;

import lombok.Getter;

@Getter
public enum FaultTypeEnum {

    ELECTRICAL("electrical", "电气故障"),
    MECHANICAL("mechanical", "机械故障"),
    PLUMBING("plumbing", "管道故障"),
    HARDWARE("hardware", "硬件故障"),
    SOFTWARE("software", "软件故障"),
    NETWORK("network", "网络故障"),
    OTHER("other", "其他故障");

    private final String code;
    private final String desc;

    FaultTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getDescByCode(String code) {
        for (FaultTypeEnum type : values()) {
            if (type.getCode().equals(code)) {
                return type.getDesc();
            }
        }
        return null;
    }
}
