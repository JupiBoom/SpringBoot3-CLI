package com.rosy.main.enums;

/**
 * 报修状态枚举
 *
 * @author Rosy
 * @since 2025-01-31
 */
public enum RepairStatusEnum {
    PENDING(0, "待处理"),
    IN_PROGRESS(1, "维修中"),
    COMPLETED(2, "已完成");

    private final int code;
    private final String description;

    RepairStatusEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static RepairStatusEnum getByCode(int code) {
        for (RepairStatusEnum status : values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        return null;
    }
}