package com.rosy.main.enums;

import lombok.Getter;

/**
 * 活动分类枚举
 */
@Getter
public enum ActivityCategoryEnum {

    ENVIRONMENT(1, "环保"),
    ELDERLY_CARE(2, "助老"),
    EDUCATION(3, "教育"),
    MEDICAL(4, "医疗");

    private final Integer code;
    private final String desc;

    ActivityCategoryEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ActivityCategoryEnum fromCode(Integer code) {
        for (ActivityCategoryEnum activityCategory : values()) {
            if (activityCategory.getCode().equals(code)) {
                return activityCategory;
            }
        }
        return null;
    }
}
