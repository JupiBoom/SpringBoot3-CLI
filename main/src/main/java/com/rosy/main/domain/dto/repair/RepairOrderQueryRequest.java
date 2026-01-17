package com.rosy.main.domain.dto.repair;

import com.rosy.common.domain.entity.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class RepairOrderQueryRequest extends PageRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String orderNo;

    private String deviceType;

    private String faultType;

    private Byte priority;

    private Byte status;

    private Long reporterId;

    private Long assigneeId;

    private String startTime;

    private String endTime;
}
