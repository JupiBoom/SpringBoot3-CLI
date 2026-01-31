package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class RepairOrderVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String orderNo;

    private Long userId;

    private String deviceType;

    private String deviceLocation;

    private String faultType;

    private String description;

    private String photoUrls;

    private Byte priority;

    private String priorityDesc;

    private Byte status;

    private String statusDesc;

    private Long staffId;

    private String staffName;

    private String staffPhone;

    private Byte assignType;

    private String assignTypeDesc;

    private LocalDateTime assignTime;

    private LocalDateTime acceptTime;

    private LocalDateTime completedTime;

    private Integer responseMinutes;

    private LocalDateTime createTime;

    private Boolean hasEvaluation;

    private RepairEvaluationVO evaluation;
}
