package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class RepairRecordVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long orderId;

    private Long staffId;

    private String staffName;

    private Byte actionType;

    private String actionTypeDesc;

    private String content;

    private String photoUrls;

    private String location;

    private LocalDateTime createTime;
}
