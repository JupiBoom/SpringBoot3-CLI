package com.rosy.common.enums;

import lombok.Getter;

@Getter
public enum ActivityStatusEnum {

    RECRUITING(1, "招募中"),
    ONGOING(2, "进行中"),
    COMPLETED(3, "已完成");

    private final Integer value;
    private final String text;

    ActivityStatusEnum(Integer value, String text) {
        this.value = value;
        this.text = text;
    }

    public static String getTextByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (ActivityStatusEnum status : values()) {
            if (status.getValue().equals(value)) {
                return status.getText();
            }
        }
        return null;
    }
}
