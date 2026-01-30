package com.rosy.main.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 会议预约VO类
 */
@Data
public class MeetingBookingVO {

    /**
     * ID
     */
    private Long id;

    /**
     * 会议主题
     */
    private String meetingTitle;

    /**
     * 会议室ID
     */
    private Long roomId;

    /**
     * 会议室名称
     */
    private String roomName;

    /**
     * 预约人ID
     */
    private Long bookerId;

    /**
     * 预约人姓名
     */
    private String bookerName;

    /**
     * 会议开始时间
     */
    private LocalDateTime startTime;

    /**
     * 会议结束时间
     */
    private LocalDateTime endTime;

    /**
     * 会议事由
     */
    private String reason;

    /**
     * 参会人员
     */
    private String attendees;

    /**
     * 状态：0-待审批，1-已通过，2-已驳回，3-已取消
     */
    private Integer status;

    /**
     * 状态描述
     */
    private String statusDesc;

    /**
     * 审批人ID
     */
    private Long approveId;

    /**
     * 审批人姓名
     */
    private String approveName;

    /**
     * 审批时间
     */
    private LocalDateTime approveTime;

    /**
     * 审批备注
     */
    private String approveRemark;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}