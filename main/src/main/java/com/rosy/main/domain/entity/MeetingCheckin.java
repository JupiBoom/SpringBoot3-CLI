package com.rosy.main.domain.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;

/**
 * 会议签到实体类
 */
@TableName("meeting_checkin")
public class MeetingCheckin {

    /**
     * ID，必须为正整数
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 预约ID
     */
    private Long bookingId;

    /**
     * 签到人ID
     */
    private Long userId;

    /**
     * 签到时间
     */
    private LocalDateTime checkinTime;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    // Getter and Setter methods
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getCheckinTime() {
        return checkinTime;
    }

    public void setCheckinTime(LocalDateTime checkinTime) {
        this.checkinTime = checkinTime;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}