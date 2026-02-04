package com.rosy.main.domain.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 论坛帖子类型枚举
 */
@Getter
public enum ForumPostTypeEnum {

    EXPERIENCE(1, "经验分享"),
    REVIEW(2, "活动回顾"),
    QUESTION(3, "问题咨询"),
    OTHER(4, "其他");

    private final int code;
    private final String desc;

    ForumPostTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private static final Map<Integer, ForumPostTypeEnum> CODE_MAP = Arrays.stream(values())
            .collect(Collectors.toMap(ForumPostTypeEnum::getCode, e -> e));

    public static ForumPostTypeEnum getByCode(Integer code) {
        return CODE_MAP.get(code);
    }

    public static String getDescByCode(Integer code) {
        ForumPostTypeEnum enumValue = CODE_MAP.get(code);
        return enumValue != null ? enumValue.getDesc() : "";
    }
}
