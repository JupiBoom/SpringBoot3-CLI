package com.rosy.main.service;

import com.rosy.main.domain.vo.StatisticsVO;

import java.util.List;

public interface IStatisticsService {

    StatisticsVO getTeacherCourseCount();

    StatisticsVO getStudentAttendanceRate();

    List<StatisticsVO> getTopCoursesByDiscussion();

    List<StatisticsVO> getTopTeachersByCourses();
}
