package com.rosy.meeting.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 会议预约创建VO
 */
@Schema(description = "会议预约创建请求")
public class MeetingBookingCreateVO {

    /**
     * 会议室ID
     */
    @Schema(description = "会议室ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long meetingRoomId;

    /**
     * 开始时间
     */
    @Schema(description = "开始时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @Schema(description = "结束时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime endTime;

    /**
     * 事由
     */
    @Schema(description = "事由", requiredMode = Schema.RequiredMode.REQUIRED)
    private String reason;

    /**
     * 参会人数
     */
    @Schema(description = "参会人数", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer participantCount;

    public Long getMeetingRoomId() {
        return meetingRoomId;
    }

    public void setMeetingRoomId(Long meetingRoomId) {
        this.meetingRoomId = meetingRoomId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Integer getParticipantCount() {
        return participantCount;
    }

    public void setParticipantCount(Integer participantCount) {
        this.participantCount = participantCount;
    }
}