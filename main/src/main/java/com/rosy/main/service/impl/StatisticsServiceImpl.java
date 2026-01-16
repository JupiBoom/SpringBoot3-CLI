package com.rosy.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rosy.main.domain.entity.Course;
import com.rosy.main.domain.entity.StudentCourse;
import com.rosy.main.domain.vo.StatisticsVO;
import com.rosy.main.mapper.AttendanceMapper;
import com.rosy.main.mapper.CourseDiscussionMapper;
import com.rosy.main.service.ICourseService;
import com.rosy.main.service.IStatisticsService;
import com.rosy.main.service.ITeacherService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatisticsServiceImpl implements IStatisticsService {

 private final ICourseService courseService;
 private final ITeacherService teacherService;
 private final AttendanceMapper attendanceMapper;
 private final CourseDiscussionMapper discussionMapper;

 public StatisticsServiceImpl(ICourseService courseService,
 ITeacherService teacherService,
 AttendanceMapper attendanceMapper,
 CourseDiscussionMapper discussionMapper) {
 this.courseService = courseService;
 this.teacherService = teacherService;
 this.attendanceMapper = attendanceMapper;
 this.discussionMapper = discussionMapper;
 }

 @Override
 public StatisticsVO getTeacherCourseCount() {
 List<Course> courses = courseService.list();
 Map<Long, Integer> teacherCourseCount = new HashMap<>();
 courses.forEach(course -> {
 teacherCourseCount.merge(course.getTeacherId(), 1, Integer::sum);
 });
 StatisticsVO vo = new StatisticsVO();
 vo.setTotalCount((long) courses.size());
 vo.setDetail(teacherCourseCount);
 return vo;
 }

 @Override
 public StatisticsVO getStudentAttendanceRate() {
 List<StudentCourse> studentCourses = new ArrayList<>();
 StatisticsVO vo = new StatisticsVO();
 vo.setTotalCount((long) studentCourses.size());
 return vo;
 }

 @Override
 public List<StatisticsVO> getTopCoursesByDiscussion() {
 List<StatisticsVO> result = new ArrayList<>();
 return result;
 }

 @Override
 public List<StatisticsVO> getTopTeachersByCourses() {
 List<StatisticsVO> result = new ArrayList<>();
 return result;
 }
}
