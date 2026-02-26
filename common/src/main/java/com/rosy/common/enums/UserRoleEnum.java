package com.rosy.common.enums;

import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum UserRoleEnum {

    ADMIN("管理员", "admin", (byte) 0),
    VOLUNTEER("志愿者", "volunteer", (byte) 1),
    ORGANIZER("组织者", "organizer", (byte) 2);

    private final String text;

    private final String value;

    private final byte code;

    UserRoleEnum(String text, String value, byte code) {
        this.text = text;
        this.value = value;
        this.code = code;
    }

    public static List<String> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    public static UserRoleEnum getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (UserRoleEnum anEnum : UserRoleEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

    public static UserRoleEnum getByCode(byte code) {
        for (UserRoleEnum value : values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        return null;
    }
}
