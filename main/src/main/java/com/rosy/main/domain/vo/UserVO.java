package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class UserVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String username;

    private String realName;

    private String phone;

    private String email;

    private String avatar;

    private Byte role;

    private String roleName;

    private Byte status;

    private String statusName;

    private LocalDateTime createTime;
}
