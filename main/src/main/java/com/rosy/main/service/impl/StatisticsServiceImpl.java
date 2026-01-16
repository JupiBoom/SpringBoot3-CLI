package com.rosy.main.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.entity.Attendance;
import com.rosy.main.domain.entity.Course;
import com.rosy.main.domain.entity.CourseStudent;
import com.rosy.main.domain.entity.Student;
import com.rosy.main.domain.entity.Teacher;
import com.rosy.main.domain.vo.StudentAttendanceStatsVO;
import com.rosy.main.domain.vo.TeacherTeachingStatsVO;
import com.rosy.main.mapper.AttendanceMapper;
import com.rosy.main.mapper.CourseMapper;
import com.rosy.main.mapper.CourseStudentMapper;
import com.rosy.main.mapper.StudentMapper;
import com.rosy.main.mapper.TeacherMapper;
import com.rosy.main.service.IStatisticsService;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatisticsServiceImpl implements IStatisticsService {

    @Resource
    private StudentMapper studentMapper;

    @Resource
    private CourseMapper courseMapper;

    @Resource
    private TeacherMapper teacherMapper;

    @Resource
    private AttendanceMapper attendanceMapper;

    @Resource
    private CourseStudentMapper courseStudentMapper;

    @Override
    public StudentAttendanceStatsVO getStudentAttendanceStats(Long studentId, Long courseId, LocalDate startDate, LocalDate endDate) {
        Student student = studentMapper.selectById(studentId);
        if (student == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "学生不存在");
        }
        Course course = courseMapper.selectById(courseId);
        if (course == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "课程不存在");
        }
        LambdaQueryWrapper<Attendance> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Attendance::getStudentId, studentId)
                .eq(Attendance::getCourseId, courseId);
        if (startDate != null) {
            wrapper.ge(Attendance::getAttendanceDate, startDate);
        }
        if (endDate != null) {
            wrapper.le(Attendance::getAttendanceDate, endDate);
        }
        List<Attendance> attendances = attendanceMapper.selectList(wrapper);
        int totalClasses = attendances.size();
        int attendedClasses = (int) attendances.stream().filter(a -> a.getStatus() == 1).count();
        int absentClasses = (int) attendances.stream().filter(a -> a.getStatus() == 2).count();
        int leaveClasses = (int) attendances.stream().filter(a -> a.getStatus() == 3).count();
        StudentAttendanceStatsVO statsVO = new StudentAttendanceStatsVO();
        statsVO.setStudentId(studentId);
        statsVO.setStudentName(student.getName());
        statsVO.setCourseId(courseId);
        statsVO.setCourseName(course.getName());
        statsVO.setTotalClasses(totalClasses);
        statsVO.setAttendedClasses(attendedClasses);
        statsVO.setAbsentClasses(absentClasses);
        statsVO.setLeaveClasses(leaveClasses);
        if (totalClasses > 0) {
            statsVO.setAttendanceRate(new BigDecimal(attendedClasses)
                    .multiply(new BigDecimal("100"))
                    .divide(new BigDecimal(totalClasses), 2, RoundingMode.HALF_UP));
            statsVO.setLeaveRate(new BigDecimal(leaveClasses)
                    .multiply(new BigDecimal("100"))
                    .divide(new BigDecimal(totalClasses), 2, RoundingMode.HALF_UP));
        } else {
            statsVO.setAttendanceRate(BigDecimal.ZERO);
            statsVO.setLeaveRate(BigDecimal.ZERO);
        }
        return statsVO;
    }

    @Override
    public List<StudentAttendanceStatsVO> getStudentAttendanceStatsList(Long courseId, LocalDate startDate, LocalDate endDate) {
        Course course = courseMapper.selectById(courseId);
        if (course == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "课程不存在");
        }
        LambdaQueryWrapper<CourseStudent> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CourseStudent::getCourseId, courseId);
        List<CourseStudent> courseStudents = courseStudentMapper.selectList(wrapper);
        return courseStudents.stream()
                .map(cs -> getStudentAttendanceStats(cs.getStudentId(), courseId, startDate, endDate))
                .collect(Collectors.toList());
    }

    @Override
    public TeacherTeachingStatsVO getTeacherTeachingStats(Long teacherId) {
        Teacher teacher = teacherMapper.selectById(teacherId);
        if (teacher == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "教师不存在");
        }
        LambdaQueryWrapper<Course> courseWrapper = new LambdaQueryWrapper<>();
        courseWrapper.eq(Course::getTeacherId, teacherId);
        List<Course> courses = courseMapper.selectList(courseWrapper);
        int totalCourses = courses.size();
        int activeCourses = (int) courses.stream().filter(c -> c.getStatus() == 1).count();
        int totalStudents = courses.stream().mapToInt(Course::getCurrentStudents).sum();
        List<Long> courseIds = courses.stream().map(Course::getId).collect(Collectors.toList());
        LambdaQueryWrapper<Attendance> attendanceWrapper = new LambdaQueryWrapper<>();
        attendanceWrapper.in(Attendance::getCourseId, courseIds);
        List<Attendance> attendances = attendanceMapper.selectList(attendanceWrapper);
        int totalClasses = attendances.size();
        int attendedClasses = (int) attendances.stream().filter(a -> a.getStatus() == 1).count();
        BigDecimal averageAttendanceRate = totalClasses > 0
                ? new BigDecimal(attendedClasses)
                .multiply(new BigDecimal("100"))
                .divide(new BigDecimal(totalClasses), 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;
        TeacherTeachingStatsVO statsVO = new TeacherTeachingStatsVO();
        statsVO.setTeacherId(teacherId);
        statsVO.setTeacherName(teacher.getName());
        statsVO.setTotalCourses(totalCourses);
        statsVO.setActiveCourses(activeCourses);
        statsVO.setTotalStudents(totalStudents);
        statsVO.setTotalClasses(totalClasses);
        statsVO.setAverageAttendanceRate(averageAttendanceRate);
        return statsVO;
    }

    @Override
    public List<TeacherTeachingStatsVO> getAllTeacherTeachingStats() {
        List<Teacher> teachers = teacherMapper.selectList(null);
        return teachers.stream()
                .map(teacher -> getTeacherTeachingStats(teacher.getId()))
                .collect(Collectors.toList());
    }
}
