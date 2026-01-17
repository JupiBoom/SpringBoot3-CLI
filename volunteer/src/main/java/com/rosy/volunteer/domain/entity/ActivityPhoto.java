package com.rosy.volunteer.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("activity_photo")
public class ActivityPhoto {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long activityId;
    private Long uploaderId;
    private String uploaderName;
    private String photoUrl;
    private String thumbnailUrl;
    private String description;
    private Integer likeCount;
    private Integer sortOrder;
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
