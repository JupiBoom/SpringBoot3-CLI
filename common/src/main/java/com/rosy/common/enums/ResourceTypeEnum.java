package com.rosy.common.enums;

import lombok.Getter;

@Getter
public enum ResourceTypeEnum {

    COURSEWARE((byte) 1, "课件"),
    VIDEO((byte) 2, "视频"),
    DOCUMENT((byte) 3, "文档"),
    OTHER((byte) 4, "其他");

    private final Byte code;
    private final String desc;

    ResourceTypeEnum(Byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}