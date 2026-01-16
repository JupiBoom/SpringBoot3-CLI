package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class StudentVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String studentNo;

    private String name;

    private Byte gender;

    private String grade;

    private String major;

    private String className;

    private String phone;

    private String email;

    private Byte status;

    private Long creatorId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
