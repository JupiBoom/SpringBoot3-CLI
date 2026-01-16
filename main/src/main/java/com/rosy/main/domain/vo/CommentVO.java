package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class CommentVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long courseId;

    private String courseName;

    private Long userId;

    private String userName;

    private Byte userType;

    private String content;

    private Long parentId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
