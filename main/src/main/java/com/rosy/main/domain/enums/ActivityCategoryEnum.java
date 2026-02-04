package com.rosy.main.domain.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 活动分类枚举
 */
@Getter
public enum ActivityCategoryEnum {

    ENVIRONMENT(1, "环保"),
    ELDERLY_CARE(2, "助老"),
    EDUCATION(3, "教育"),
    MEDICAL(4, "医疗"),
    OTHER(5, "其他");

    private final int code;
    private final String desc;

    ActivityCategoryEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private static final Map<Integer, ActivityCategoryEnum> CODE_MAP = Arrays.stream(values())
            .collect(Collectors.toMap(ActivityCategoryEnum::getCode, e -> e));

    public static ActivityCategoryEnum getByCode(Integer code) {
        return CODE_MAP.get(code);
    }

    public static String getDescByCode(Integer code) {
        ActivityCategoryEnum enumValue = CODE_MAP.get(code);
        return enumValue != null ? enumValue.getDesc() : "";
    }
}
