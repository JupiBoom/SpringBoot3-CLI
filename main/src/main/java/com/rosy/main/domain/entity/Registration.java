package com.rosy.main.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("registration")
public class Registration {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long activityId;
    private Long userId;
    private String status;
    private String auditType;
    private Long auditId;
    private LocalDateTime auditTime;
    private String auditReason;
    private Integer reminded;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @TableLogic
    private Integer isDeleted;
}
