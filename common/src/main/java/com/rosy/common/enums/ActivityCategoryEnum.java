package com.rosy.common.enums;

import lombok.Getter;

@Getter
public enum ActivityCategoryEnum {

    ENVIRONMENTAL(1, "环保"),
    ELDERLY_CARE(2, "助老"),
    EDUCATION(3, "教育"),
    MEDICAL(4, "医疗");

    private final Integer value;
    private final String text;

    ActivityCategoryEnum(Integer value, String text) {
        this.value = value;
        this.text = text;
    }

    public static String getTextByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (ActivityCategoryEnum category : values()) {
            if (category.getValue().equals(value)) {
                return category.getText();
            }
        }
        return null;
    }
}
