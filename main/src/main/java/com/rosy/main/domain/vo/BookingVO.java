package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class BookingVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long roomId;

    private String roomName;

    private Long userId;

    private String userName;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String subject;

    private Integer attendeesCount;

    private String remarks;

    private Byte status;

    private String statusText;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
