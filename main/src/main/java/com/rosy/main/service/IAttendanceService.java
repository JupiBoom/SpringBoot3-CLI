package com.rosy.main.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.main.domain.dto.attendance.AttendanceQueryRequest;
import com.rosy.main.domain.entity.Attendance;
import com.rosy.main.domain.vo.AttendanceStatisticsVO;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface IAttendanceService extends IService<Attendance> {

    Boolean signIn(Long courseId, Long studentId);

    Boolean updateAttendanceStatus(Long id, Byte status, String remark);

    Boolean deleteAttendance(IdRequest idRequest);

    Attendance getAttendanceById(Long id);

    Page<Attendance> listAttendances(int current, int size, Long courseId, Long studentId, LocalDate attendDate);

    List<Map<String, Object>> getAttendanceStatistics(Long courseId);

    List<AttendanceStatisticsVO> getStudentAttendanceRate(Long studentId);

    LambdaQueryWrapper<Attendance> getQueryWrapper(AttendanceQueryRequest attendanceQueryRequest);

    List<Map<String, Object>> getCourseAttendanceStatistics(Long courseId);

    List<Attendance> getAbsentRecords(Long courseId);
}