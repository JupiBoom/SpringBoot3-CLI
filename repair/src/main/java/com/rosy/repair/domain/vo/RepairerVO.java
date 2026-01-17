package com.rosy.repair.domain.vo;

import lombok.Data;

@Data
public class RepairerVO {

    private Long id;

    private Long userId;

    private String name;

    private String phone;

    private String skill;

    private Integer status;

    private Integer orderCount;
}
