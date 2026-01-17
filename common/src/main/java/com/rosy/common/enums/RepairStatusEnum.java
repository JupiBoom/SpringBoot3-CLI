package com.rosy.common.enums;

import lombok.Getter;

/**
 * 报修工单状态枚举
 */
@Getter
public enum RepairStatusEnum {

    PENDING("pending", "待处理"),
    ASSIGNED("assigned", "已分配"),
    PROCESSING("processing", "维修中"),
    COMPLETED("completed", "已完成"),
    CANCELLED("cancelled", "已取消");

    private final String code;
    private final String desc;

    RepairStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static RepairStatusEnum getByCode(String code) {
        for (RepairStatusEnum status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}
