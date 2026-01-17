package com.rosy.main.domain.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 报修工单VO
 */
@Data
public class RepairOrderVO {

    private Long id;

    private String orderNo;

    private Long userId;

    private String userName;

    private String userPhone;

    private Long equipmentId;

    private String equipmentName;

    private String equipmentType;

    private String location;

    private String faultType;

    private String faultDescription;

    private List<String> faultImages;

    private String priority;

    private String priorityDesc;

    private String status;

    private String statusDesc;

    private Long repairmanId;

    private String repairmanName;

    private LocalDateTime assignmentTime;

    private LocalDateTime acceptTime;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer responseTime;

    private Integer repairDuration;

    private BigDecimal totalCost;

    private String remark;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private List<RepairRecordVO> records;

    private RepairEvaluationVO evaluation;
}
