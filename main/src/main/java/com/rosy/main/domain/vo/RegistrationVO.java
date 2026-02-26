package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class RegistrationVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long activityId;

    private String activityTitle;

    private Long userId;

    private String userName;

    private String userPhone;

    private Byte status;

    private String statusDesc;

    private Long auditUserId;

    private String auditUserName;

    private LocalDateTime auditTime;

    private String auditRemark;

    private LocalDateTime checkInTime;

    private String checkInLocation;

    private LocalDateTime checkOutTime;

    private String checkOutLocation;

    private LocalDateTime createTime;
}
