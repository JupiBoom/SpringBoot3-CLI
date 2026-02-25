package com.rosy.common.enums;

public enum ActivityCategoryEnum {
    ENVIRONMENTAL("环保"),
    ELDERLY("助老"),
    EDUCATION("教育"),
    MEDICAL("医疗");

    private final String desc;

    ActivityCategoryEnum(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
