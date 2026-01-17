package com.rosy.main.domain.vo.repair;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class RepairPersonnelVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long userId;

    private String name;

    private String phone;

    private String specialty;

    private Byte skillLevel;

    private String skillLevelDesc;

    private Byte status;

    private String statusDesc;

    private Integer totalOrders;

    private Integer completedOrders;

    private BigDecimal avgRating;

    private LocalDateTime createTime;
}
