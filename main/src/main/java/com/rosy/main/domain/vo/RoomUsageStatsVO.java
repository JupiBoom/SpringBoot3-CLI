package com.rosy.main.domain.vo;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 会议室使用统计VO类
 */
public class RoomUsageStatsVO {

    /**
     * ID
     */
    private Long id;

    /**
     * 会议室ID
     */
    private Long roomId;

    /**
     * 会议室名称
     */
    private String roomName;

    /**
     * 统计日期
     */
    private LocalDate statsDate;

    /**
     * 总预约次数
     */
    private Integer totalBookings;

    /**
     * 实际使用次数（有签到的会议）
     */
    private Integer actualUsage;

    /**
     * 使用率（实际使用次数/总预约次数）
     */
    private BigDecimal usageRate;

    /**
     * 总使用时长（小时）
     */
    private BigDecimal totalHours;

    /**
     * 平均使用时长（小时）
     */
    private BigDecimal avgHours;

    // Getter and Setter methods
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public LocalDate getStatsDate() {
        return statsDate;
    }

    public void setStatsDate(LocalDate statsDate) {
        this.statsDate = statsDate;
    }

    public Integer getTotalBookings() {
        return totalBookings;
    }

    public void setTotalBookings(Integer totalBookings) {
        this.totalBookings = totalBookings;
    }

    public Integer getActualUsage() {
        return actualUsage;
    }

    public void setActualUsage(Integer actualUsage) {
        this.actualUsage = actualUsage;
    }

    public BigDecimal getUsageRate() {
        return usageRate;
    }

    public void setUsageRate(BigDecimal usageRate) {
        this.usageRate = usageRate;
    }

    public BigDecimal getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(BigDecimal totalHours) {
        this.totalHours = totalHours;
    }

    public BigDecimal getAvgHours() {
        return avgHours;
    }

    public void setAvgHours(BigDecimal avgHours) {
        this.avgHours = avgHours;
    }
}