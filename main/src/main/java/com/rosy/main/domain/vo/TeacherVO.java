package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class TeacherVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String teacherNo;

    private String name;

    private Byte gender;

    private String title;

    private String department;

    private String phone;

    private String email;

    private Byte status;

    private Long creatorId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
