package com.rosy.meeting.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("meeting_reservation")
public class MeetingReservation {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long roomId;
    
    private Long applicantId;
    
    private String applicantName;
    
    private String meetingSubject;
    
    private String meetingReason;
    
    private LocalDateTime startTime;
    
    private LocalDateTime endTime;
    
    private String attendees;
    
    private Integer status;
    
    private String remark;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;
}