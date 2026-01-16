package com.rosy.course.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 教师上课量VO
 *
 * @author Rosy
 * @since 2025-01-16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeachingCountVO {

    /**
     * 课程ID
     */
    private Long courseId;

    /**
     * 课程名称
     */
    private String courseName;

    /**
     * 总授课天数
     */
    private Integer totalTeachingDays;

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

    public Integer getTotalTeachingDays() {
        return totalTeachingDays;
    }

    public void setTotalTeachingDays(Integer totalTeachingDays) {
        this.totalTeachingDays = totalTeachingDays;
    }
}