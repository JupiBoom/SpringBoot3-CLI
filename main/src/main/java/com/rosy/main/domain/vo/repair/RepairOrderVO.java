package com.rosy.main.domain.vo.repair;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class RepairOrderVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String orderNo;

    private Long deviceId;

    private String deviceType;

    private String location;

    private String faultType;

    private String faultDesc;

    private Byte priority;

    private String priorityDesc;

    private Byte status;

    private String statusDesc;

    private Long reporterId;

    private String reporterName;

    private String reporterPhone;

    private Long assigneeId;

    private String assigneeName;

    private LocalDateTime assignTime;

    private LocalDateTime acceptTime;

    private LocalDateTime completeTime;

    private String repairResult;

    private LocalDateTime createTime;

    private List<String> photoUrls;

    private List<RepairRecordVO> records;

    private RepairEvaluationVO evaluation;
}
