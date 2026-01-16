package com.rosy.main.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationTypeEnum {

    APPROVAL_APPROVED(1, "审批通过"),
    APPROVAL_REJECTED(2, "审批驳回"),
    MEETING_REMINDER(3, "会议开始提醒"),
    MEETING_CANCELLED(4, "会议取消");

    private final Integer code;
    private final String desc;

    public static String getDescByCode(Integer code) {
        for (NotificationTypeEnum type : values()) {
            if (type.getCode().equals(code)) {
                return type.getDesc();
            }
        }
        return "未知类型";
    }
}
