package com.rosy.volunteer.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("forum_comment")
public class ForumComment {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long postId;
    private Long parentId;
    private Long authorId;
    private String authorName;
    private String authorAvatar;
    private String content;
    private Integer likeCount;
    private Integer status;
    private Long creatorId;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    private Long updaterId;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @TableLogic
    private Integer isDeleted;
}
