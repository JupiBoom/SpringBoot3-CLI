package com.rosy.main.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class ForumPostVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long activityId;

    private String activityTitle;

    private Long userId;

    private String userName;

    private String userAvatar;

    private String title;

    private String content;

    private Byte type;

    private String typeName;

    private Integer viewCount;

    private Integer likeCount;

    private Integer commentCount;

    private Byte isTop;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
