package com.rosy.common.enums;

import lombok.Getter;

@Getter
public enum LiveRoomStatusEnum {

    NOT_STARTED((byte) 0, "未开始"),
    LIVING((byte) 1, "直播中"),
    ENDED((byte) 2, "已结束");

    private final Byte value;
    private final String text;

    LiveRoomStatusEnum(Byte value, String text) {
        this.value = value;
        this.text = text;
    }

    public static LiveRoomStatusEnum getByValue(Byte value) {
        if (value == null) {
            return null;
        }
        for (LiveRoomStatusEnum status : values()) {
            if (status.getValue().equals(value)) {
                return status;
            }
        }
        return null;
    }
}
