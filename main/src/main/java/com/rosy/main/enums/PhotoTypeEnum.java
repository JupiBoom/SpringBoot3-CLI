package com.rosy.main.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PhotoTypeEnum {

    FAULT(1, "故障照片"),
    REPAIR_PROCESS(2, "维修过程照片"),
    COMPLETE(3, "完成照片");

    private final Integer code;
    private final String desc;

    public static PhotoTypeEnum getByCode(Integer code) {
        for (PhotoTypeEnum photoTypeEnum : values()) {
            if (photoTypeEnum.getCode().equals(code)) {
                return photoTypeEnum;
            }
        }
        return null;
    }
}
