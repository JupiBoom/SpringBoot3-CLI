package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class PhotoVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long activityId;

    private String activityTitle;

    private Long userId;

    private String userName;

    private String url;

    private String thumbnailUrl;

    private String description;

    private Integer likeCount;

    private Byte status;

    private LocalDateTime createTime;
}
