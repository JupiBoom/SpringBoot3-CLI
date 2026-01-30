package com.rosy.common.enums;

import lombok.Getter;

@Getter
public enum RoomEquipmentEnum {

    PROJECTOR("projector", "投影仪"),
    WHITEBOARD("whiteboard", "白板"),
    VIDEO_CONFERENCE("video_conference", "视频会议"),
    AUDIO_SYSTEM("audio_system", "音响系统"),
    AIR_CONDITIONER("air_conditioner", "空调"),
    WIFI("wifi", "WiFi"),
    TELEPHONE("telephone", "电话"),
    COMPUTER("computer", "电脑");

    private final String code;
    private final String text;

    RoomEquipmentEnum(String code, String text) {
        this.code = code;
        this.text = text;
    }

    public static RoomEquipmentEnum getByCode(String code) {
        if (code == null) {
            return null;
        }
        for (RoomEquipmentEnum equipment : RoomEquipmentEnum.values()) {
            if (equipment.getCode().equals(code)) {
                return equipment;
            }
        }
        return null;
    }
}
