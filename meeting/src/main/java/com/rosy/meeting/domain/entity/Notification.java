package com.rosy.meeting.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("notification")
public class Notification {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long userId;
    
    private Integer notificationType;
    
    private String title;
    
    private String content;
    
    private Long relatedId;
    
    private Integer isRead;
    
    private LocalDateTime readTime;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;
}