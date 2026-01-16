package com.rosy.main.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("course_discussion")
public class CourseDiscussion implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("course_id")
    private Long courseId;

    @TableField("user_id")
    private Long userId;

    @TableField("user_type")
    private Byte userType;

    @TableField("content")
    private String content;

    @TableField("parent_id")
    private Long parentId;

    @TableField("reply_count")
    private Integer replyCount;

    @TableField("like_count")
    private Integer likeCount;

    @TableField(value = "creator_id", fill = FieldFill.INSERT)
    private Long creatorId;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "updater_id", fill = FieldFill.INSERT_UPDATE)
    private Long updaterId;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(value = "is_deleted", fill = FieldFill.INSERT)
    @TableLogic
    private Byte isDeleted;
}