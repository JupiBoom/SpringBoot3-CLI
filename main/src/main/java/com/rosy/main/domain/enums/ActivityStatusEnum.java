package com.rosy.main.domain.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 活动状态枚举
 */
@Getter
public enum ActivityStatusEnum {

    RECRUITING(0, "招募中"),
    IN_PROGRESS(1, "进行中"),
    COMPLETED(2, "已完成"),
    CANCELLED(3, "已取消");

    private final int code;
    private final String desc;

    ActivityStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private static final Map<Integer, ActivityStatusEnum> CODE_MAP = Arrays.stream(values())
            .collect(Collectors.toMap(ActivityStatusEnum::getCode, e -> e));

    public static ActivityStatusEnum getByCode(Integer code) {
        return CODE_MAP.get(code);
    }

    public static String getDescByCode(Integer code) {
        ActivityStatusEnum enumValue = CODE_MAP.get(code);
        return enumValue != null ? enumValue.getDesc() : "";
    }
}
