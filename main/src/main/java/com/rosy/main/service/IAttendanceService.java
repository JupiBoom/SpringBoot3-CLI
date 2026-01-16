package com.rosy.main.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.Attendance;
import com.rosy.main.domain.vo.AttendanceVO;

import java.time.LocalDate;
import java.util.List;

public interface IAttendanceService extends IService<Attendance> {

    AttendanceVO getAttendanceVO(Attendance attendance);

    LambdaQueryWrapper<Attendance> getQueryWrapper(Long courseId, Long studentId, LocalDate startDate, LocalDate endDate, Byte status);

    boolean checkIn(Long courseId, Long studentId, LocalDate date);

    boolean batchCheckIn(Long courseId, List<Long> studentIds, LocalDate date);

    List<AttendanceVO> getAttendanceByCourse(Long courseId, LocalDate date);

    List<AttendanceVO> getAttendanceByStudent(Long studentId, LocalDate startDate, LocalDate endDate);
}
