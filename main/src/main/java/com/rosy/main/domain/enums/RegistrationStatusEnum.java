package com.rosy.main.domain.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 报名状态枚举
 */
@Getter
public enum RegistrationStatusEnum {

    PENDING(0, "待审核"),
    APPROVED(1, "已通过"),
    REJECTED(2, "已拒绝"),
    CANCELLED(3, "已取消"),
    CHECKED_IN(4, "已签到"),
    CHECKED_OUT(5, "已签出");

    private final int code;
    private final String desc;

    RegistrationStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private static final Map<Integer, RegistrationStatusEnum> CODE_MAP = Arrays.stream(values())
            .collect(Collectors.toMap(RegistrationStatusEnum::getCode, e -> e));

    public static RegistrationStatusEnum getByCode(Integer code) {
        return CODE_MAP.get(code);
    }

    public static String getDescByCode(Integer code) {
        RegistrationStatusEnum enumValue = CODE_MAP.get(code);
        return enumValue != null ? enumValue.getDesc() : "";
    }
}
