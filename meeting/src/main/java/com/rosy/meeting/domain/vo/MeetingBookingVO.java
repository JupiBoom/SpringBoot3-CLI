package com.rosy.meeting.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 会议预约VO
 */
@Data
public class MeetingBookingVO {

    /**
     * 预约ID
     */
    private Long id;

    /**
     * 会议室ID
     */
    private Long meetingRoomId;

    /**
     * 会议室名称
     */
    private String meetingRoomName;

    /**
     * 预约人ID
     */
    private Long userId;

    /**
     * 预约人姓名
     */
    private String userName;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 事由
     */
    private String reason;

    /**
     * 参会人数
     */
    private Integer participantCount;

    /**
     * 状态：0-待审批，1-已通过，2-已驳回，3-已取消
     */
    private Integer status;

    /**
     * 审批人ID
     */
    private Long approverId;

    /**
     * 审批时间
     */
    private LocalDateTime approveTime;

    /**
     * 审批意见
     */
    private String approveRemark;

    /**
     * 签到人数
     */
    private Integer checkInCount;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}