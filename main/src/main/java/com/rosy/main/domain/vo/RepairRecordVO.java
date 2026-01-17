package com.rosy.main.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 维修记录VO
 */
@Data
public class RepairRecordVO {

    private Long id;

    private Long orderId;

    private String recordType;

    private String recordTypeDesc;

    private String content;

    private List<String> images;

    private Long operatorId;

    private String operatorName;

    private LocalDateTime createTime;
}
