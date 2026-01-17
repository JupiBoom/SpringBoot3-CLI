package com.rosy.main.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationTypeEnum {

    REPAIR_REPORT(1, "报修通知"),
    ASSIGN(2, "分配通知"),
    ACCEPT(3, "接单通知"),
    COMPLETE(4, "完成通知"),
    EVALUATION(5, "评价通知");

    private final Integer code;
    private final String desc;

    public static NotificationTypeEnum getByCode(Integer code) {
        for (NotificationTypeEnum notificationTypeEnum : values()) {
            if (notificationTypeEnum.getCode().equals(code)) {
                return notificationTypeEnum;
            }
        }
        return null;
    }
}
