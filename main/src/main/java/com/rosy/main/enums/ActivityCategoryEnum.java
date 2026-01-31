package com.rosy.main.enums;

/**
 * 活动分类枚举
 */
public enum ActivityCategoryEnum {
    ENVIRONMENT(1, "环保"),
    ELDERLY(2, "助老"),
    EDUCATION(3, "教育"),
    MEDICAL(4, "医疗");

    private final Integer code;
    private final String desc;

    ActivityCategoryEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static ActivityCategoryEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (ActivityCategoryEnum category : values()) {
            if (category.getCode().equals(code)) {
                return category;
            }
        }
        return null;
    }
}