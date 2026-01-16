package com.rosy.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.course.domain.entity.Attendance;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 考勤记录表 Service 接口
 * </p>
 *
 * @author Rosy
 * @since 2025-01-16
 */
public interface IAttendanceService extends IService<Attendance> {

    /**
     * 签到
     */
    boolean signIn(Attendance attendance);

    /**
     * 申请请假
     */
    boolean applyLeave(Attendance attendance);

    /**
     * 审批请假
     */
    boolean approveLeave(Long id, Byte status, Long approverId);

    /**
     * 统计学生出勤率
     */
    List<Map<String, Object>> getAttendanceRateByStudent(Long studentId);

    /**
     * 统计教师上课量
     */
    List<Map<String, Object>> getTeachingCountByTeacher(Long teacherId);

    /**
     * 统计课程缺勤情况
     */
    List<Map<String, Object>> getAbsenteeismByCourse(Long courseId);

}