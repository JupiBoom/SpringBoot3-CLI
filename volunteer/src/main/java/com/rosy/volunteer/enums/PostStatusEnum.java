package com.rosy.volunteer.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PostStatusEnum {
    NORMAL(1, "正常"),
    EXCELLENT(2, "精华"),
    TOP(3, "置顶"),
    DELETED(4, "已删除");

    private final Integer code;
    private final String desc;

    public static PostStatusEnum getByCode(Integer code) {
        for (PostStatusEnum status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}
