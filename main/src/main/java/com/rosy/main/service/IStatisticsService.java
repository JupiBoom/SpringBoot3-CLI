package com.rosy.main.service;

import com.rosy.main.domain.vo.StudentAttendanceStatsVO;
import com.rosy.main.domain.vo.TeacherTeachingStatsVO;

import java.time.LocalDate;
import java.util.List;

public interface IStatisticsService {

    StudentAttendanceStatsVO getStudentAttendanceStats(Long studentId, Long courseId, LocalDate startDate, LocalDate endDate);

    List<StudentAttendanceStatsVO> getStudentAttendanceStatsList(Long courseId, LocalDate startDate, LocalDate endDate);

    TeacherTeachingStatsVO getTeacherTeachingStats(Long teacherId);

    List<TeacherTeachingStatsVO> getAllTeacherTeachingStats();
}
