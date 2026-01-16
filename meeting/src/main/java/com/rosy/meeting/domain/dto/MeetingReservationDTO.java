package com.rosy.meeting.domain.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class MeetingReservationDTO {
    private Long id;
    
    @NotNull(message = "会议室ID不能为空")
    private Long roomId;
    
    @NotNull(message = "申请人ID不能为空")
    private Long applicantId;
    
    @NotBlank(message = "申请人姓名不能为空")
    private String applicantName;
    
    @NotBlank(message = "会议主题不能为空")
    private String meetingSubject;
    
    private String meetingReason;
    
    @NotNull(message = "开始时间不能为空")
    private LocalDateTime startTime;
    
    @NotNull(message = "结束时间不能为空")
    private LocalDateTime endTime;
    
    private String attendees;
    
    private String remark;
}