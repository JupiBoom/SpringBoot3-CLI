package com.rosy.main.domain.vo.repair;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class DeviceVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String deviceNo;

    private String deviceName;

    private String deviceType;

    private String brand;

    private String model;

    private String location;

    private LocalDate purchaseDate;

    private LocalDate warrantyDate;

    private Byte status;

    private String statusDesc;

    private LocalDateTime createTime;
}
