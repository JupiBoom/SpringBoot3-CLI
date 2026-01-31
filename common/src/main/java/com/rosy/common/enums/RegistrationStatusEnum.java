package com.rosy.common.enums;

import lombok.Getter;

@Getter
public enum RegistrationStatusEnum {

    PENDING(0, "待审核"),
    APPROVED(1, "审核通过"),
    REJECTED(2, "审核拒绝");

    private final Integer value;
    private final String text;

    RegistrationStatusEnum(Integer value, String text) {
        this.value = value;
        this.text = text;
    }

    public static String getTextByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (RegistrationStatusEnum status : values()) {
            if (status.getValue().equals(value)) {
                return status.getText();
            }
        }
        return null;
    }
}
