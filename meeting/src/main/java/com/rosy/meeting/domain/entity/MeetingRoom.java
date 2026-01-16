package com.rosy.meeting.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("meeting_room")
public class MeetingRoom {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String roomName;
    
    private String location;
    
    private Integer capacity;
    
    private String equipment;
    
    private String description;
    
    private Integer status;
    
    private Long createdBy;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;
    
    private Long updatedBy;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;
    
    @TableLogic
    private Integer isDeleted;
}