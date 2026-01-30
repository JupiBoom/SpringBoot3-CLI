package com.rosy.main.domain.vo;

import lombok.Data;

/**
 * 会议室预约请求VO类
 */
@Data
public class MeetingBookingRequestVO {

    /**
     * ID（更新时使用）
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
     * 会议开始时间
     */
    private String startTime;

    /**
     * 会议结束时间
     */
    private String endTime;

    /**
     * 会议事由
     */
    private String reason;

    /**
     * 参会人员（多个人员ID用逗号分隔）
     */
    private String attendees;
}