package com.rosy.main.domain.vo.repair;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class RepairRecordVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long orderId;

    private Byte recordType;

    private String recordTypeDesc;

    private String content;

    private List<String> photos;

    private Long operatorId;

    private String operatorName;

    private LocalDateTime createTime;
}
