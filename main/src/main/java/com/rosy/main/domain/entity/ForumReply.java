package com.rosy.main.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("forum_reply")
public class ForumReply {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long postId;
    private Long parentId;
    private Long userId;
    private String content;
    private Integer likeCount;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @TableLogic
    private Integer isDeleted;
}
