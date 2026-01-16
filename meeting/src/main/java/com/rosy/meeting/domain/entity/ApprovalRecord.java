package com.rosy.meeting.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("approval_record")
public class ApprovalRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long reservationId;
    
    private Long approverId;
    
    private String approverName;
    
    private Integer action;
    
    private String reason;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;
}