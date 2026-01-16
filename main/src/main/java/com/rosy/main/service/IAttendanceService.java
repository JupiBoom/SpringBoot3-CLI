package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.Attendance;
import com.rosy.main.domain.vo.AttendanceStatisticsVO;

import java.util.List;

public interface IAttendanceService extends IService<Attendance> {

    boolean signIn(Long courseId, Long studentId);

    List<AttendanceStatisticsVO> getAttendanceStatisticsByCourse(Long courseId);

    List<AttendanceStatisticsVO> getAttendanceStatisticsByStudent(Long studentId);

    boolean approveLeave(Long attendanceId, Byte approveStatus);
}
