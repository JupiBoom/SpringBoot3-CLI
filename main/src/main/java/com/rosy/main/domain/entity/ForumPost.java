package com.rosy.main.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("forum_post")
public class ForumPost {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long activityId;
    private Long userId;
    private String title;
    private String content;
    private Integer viewCount;
    private Integer likeCount;
    private Integer replyCount;
    private Integer isTop;
    private Integer status;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @TableLogic
    private Integer isDeleted;
}
