package com.rosy.meeting.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("check_in_record")
public class CheckInRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long reservationId;
    
    private Long userId;
    
    private String userName;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime checkInTime;
    
    private Integer checkInType;
    
    private String remark;
}