package com.rosy.main.domain.vo.repair;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class RepairEvaluationVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long orderId;

    private Byte rating;

    private String content;

    private Long evaluatorId;

    private String evaluatorName;

    private LocalDateTime createTime;
}
