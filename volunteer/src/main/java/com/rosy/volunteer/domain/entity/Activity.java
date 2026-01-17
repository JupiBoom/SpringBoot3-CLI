package com.rosy.volunteer.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("activity")
public class Activity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String title;
    private String description;
    private Long categoryId;
    private LocalDateTime activityTime;
    private String location;
    private Integer requiredVolunteers;
    private Integer registeredCount;
    private Integer status;
    private String contactName;
    private String contactPhone;
    private String coverImage;
    private Integer maxDuration;
    private Long creatorId;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    private Long updaterId;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @TableLogic
    private Integer isDeleted;
}
