package com.rosy.main.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.dto.attendance.AttendanceQueryRequest;
import com.rosy.main.domain.entity.Attendance;
import com.rosy.main.domain.vo.AttendanceVO;

/**
 * <p>
 * 考勤表 服务类
 * </p>
 *
 * @author Rosy
 * @since 2025-01-30
 */
public interface IAttendanceService extends IService<Attendance> {

    /**
     * 获取查询条件
     *
     * @param attendanceQueryRequest 考勤查询请求
     * @return 查询条件
     */
    QueryWrapper<Attendance> getQueryWrapper(AttendanceQueryRequest attendanceQueryRequest);

    /**
     * 获取考勤视图对象
     *
     * @param attendance 考勤实体
     * @return 考勤视图对象
     */
    AttendanceVO getAttendanceVO(Attendance attendance);

    /**
     * 分页获取考勤视图对象
     *
     * @param attendancePage 考勤分页对象
     * @return 考勤视图对象分页
     */
    Page<AttendanceVO> getAttendanceVOPage(Page<Attendance> attendancePage);

    /**
     * 学生签到
     *
     * @param courseId 课程ID
     * @param studentId 学生ID
     * @return 是否成功
     */
    boolean studentSignIn(Long courseId, Long studentId);

    /**
     * 批量创建考勤记录
     *
     * @param courseId 课程ID
     * @param attendanceDate 考勤日期
     * @return 是否成功
     */
    boolean batchCreateAttendance(Long courseId, String attendanceDate);
}