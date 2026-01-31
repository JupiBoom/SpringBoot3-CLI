package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class RepairStaffVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long userId;

    private String staffName;

    private String phone;

    private String specialty;

    private Byte status;

    private String statusDesc;

    private Integer orderCount;
}
