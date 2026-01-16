package com.rosy.main.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MeetingNotificationVO {

    private Long id;
    private Long reservationId;
    private Long userId;
    private Byte notificationType;
    private String notificationTypeDesc;
    private String title;
    private String content;
    private Byte isRead;
    private LocalDateTime createTime;
}
