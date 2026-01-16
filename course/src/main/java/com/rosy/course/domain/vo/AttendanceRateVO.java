package com.rosy.course.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 出勤率VO
 *
 * @author Rosy
 * @since 2025-01-16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceRateVO {

    /**
     * 课程ID
     */
    private Long courseId;

    /**
     * 课程名称
     */
    private String courseName;

    /**
     * 总课时
     */
    private Integer totalClasses;

    /**
     * 已上课时
     */
    private Integer attendedClasses;

    /**
     * 出勤率
     */
    private BigDecimal attendanceRate;

    // Getters and Setters
    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Integer getTotalClasses() {
        return totalClasses;
    }

    public void setTotalClasses(Integer totalClasses) {
        this.totalClasses = totalClasses;
    }

    public Integer getAttendedClasses() {
        return attendedClasses;
    }

    public void setAttendedClasses(Integer attendedClasses) {
        this.attendedClasses = attendedClasses;
    }

    public BigDecimal getAttendanceRate() {
        return attendanceRate;
    }

    public void setAttendanceRate(BigDecimal attendanceRate) {
        this.attendanceRate = attendanceRate;
    }
}