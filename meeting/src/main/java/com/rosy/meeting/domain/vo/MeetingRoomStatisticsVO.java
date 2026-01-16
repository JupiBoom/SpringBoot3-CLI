package com.rosy.meeting.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 会议室统计VO
 */
@Schema(description = "会议室统计信息")
public class MeetingRoomStatisticsVO {

    @Schema(description = "会议室ID")
    private Long meetingRoomId;

    @Schema(description = "会议室名称")
    private String meetingRoomName;

    @Schema(description = "总预约次数")
    private Integer totalBookings;

    @Schema(description = "已完成会议次数")
    private Integer completedMeetings;

    @Schema(description = "取消会议次数")
    private Integer cancelledMeetings;

    @Schema(description = "平均签到率")
    private Double averageCheckInRate;

    @Schema(description = "本月预约次数")
    private Integer thisMonthBookings;

    @Schema(description = "上月预约次数")
    private Integer lastMonthBookings;

    public Long getMeetingRoomId() {
        return meetingRoomId;
    }

    public void setMeetingRoomId(Long meetingRoomId) {
        this.meetingRoomId = meetingRoomId;
    }

    public String getMeetingRoomName() {
        return meetingRoomName;
    }

    public void setMeetingRoomName(String meetingRoomName) {
        this.meetingRoomName = meetingRoomName;
    }

    public Integer getTotalBookings() {
        return totalBookings;
    }

    public void setTotalBookings(Integer totalBookings) {
        this.totalBookings = totalBookings;
    }

    public Integer getCompletedMeetings() {
        return completedMeetings;
    }

    public void setCompletedMeetings(Integer completedMeetings) {
        this.completedMeetings = completedMeetings;
    }

    public Integer getCancelledMeetings() {
        return cancelledMeetings;
    }

    public void setCancelledMeetings(Integer cancelledMeetings) {
        this.cancelledMeetings = cancelledMeetings;
    }

    public Double getAverageCheckInRate() {
        return averageCheckInRate;
    }

    public void setAverageCheckInRate(Double averageCheckInRate) {
        this.averageCheckInRate = averageCheckInRate;
    }

    public Integer getThisMonthBookings() {
        return thisMonthBookings;
    }

    public void setThisMonthBookings(Integer thisMonthBookings) {
        this.thisMonthBookings = thisMonthBookings;
    }

    public Integer getLastMonthBookings() {
        return lastMonthBookings;
    }

    public void setLastMonthBookings(Integer lastMonthBookings) {
        this.lastMonthBookings = lastMonthBookings;
    }
}
