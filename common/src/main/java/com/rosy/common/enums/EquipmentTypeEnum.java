package com.rosy.common.enums;

import lombok.Getter;

/**
 * 设备类型枚举
 */
@Getter
public enum EquipmentTypeEnum {

    COMPUTER("computer", "计算机"),
    PRINTER("printer", "打印机"),
    PROJECTOR("projector", "投影仪"),
    AIR_CONDITIONER("air_conditioner", "空调"),
    PHONE("phone", "电话"),
    NETWORK_EQUIPMENT("network_equipment", "网络设备"),
    COPY_MACHINE("copy_machine", "复印机"),
    OTHER("other", "其他");

    private final String code;
    private final String desc;

    EquipmentTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static EquipmentTypeEnum getByCode(String code) {
        for (EquipmentTypeEnum type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return OTHER;
    }
}
