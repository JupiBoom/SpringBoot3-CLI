package com.rosy.main.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RepairRecordTypeEnum {

    ACCEPT(1, "接单"),
    REPAIR_PROCESS(2, "维修过程"),
    COMPLETE(3, "完成"),
    CANCEL(4, "取消");

    private final Integer code;
    private final String desc;

    public static RepairRecordTypeEnum getByCode(Integer code) {
        for (RepairRecordTypeEnum recordTypeEnum : values()) {
            if (recordTypeEnum.getCode().equals(code)) {
                return recordTypeEnum;
            }
        }
        return null;
    }
}
