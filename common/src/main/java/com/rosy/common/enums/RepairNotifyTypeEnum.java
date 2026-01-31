package com.rosy.common.enums;

import lombok.Getter;

@Getter
public enum RepairNotifyTypeEnum {

    ORDER_ASSIGN(1, "工单分配"),
    STATUS_CHANGE(2, "状态变更"),
    REPAIR_COMPLETE(3, "维修完成"),
    EVALUATION_REMIND(4, "评价提醒");

    private final Integer code;
    private final String desc;

    RepairNotifyTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getDescByCode(Integer code) {
        for (RepairNotifyTypeEnum type : values()) {
            if (type.getCode().equals(code)) {
                return type.getDesc();
            }
        }
        return null;
    }
}
