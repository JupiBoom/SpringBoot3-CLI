package com.rosy.meeting.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("meeting_room_statistics")
public class MeetingRoomStatistics {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long roomId;
    
    private LocalDate statisticsDate;
    
    private Integer totalReservations;
    
    private Integer completedReservations;
    
    private Integer cancelledReservations;
    
    private BigDecimal totalHours;
    
    private BigDecimal avgAttendance;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;
}