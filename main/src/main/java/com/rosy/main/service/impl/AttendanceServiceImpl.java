package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.enums.ErrorCode;
import com.rosy.common.exception.BusinessException;
import com.rosy.main.domain.entity.Attendance;
import com.rosy.main.domain.entity.Course;
import com.rosy.main.domain.entity.Student;
import com.rosy.main.domain.vo.AttendanceVO;
import com.rosy.main.mapper.AttendanceMapper;
import com.rosy.main.mapper.CourseMapper;
import com.rosy.main.mapper.StudentMapper;
import com.rosy.main.service.IAttendanceService;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AttendanceServiceImpl extends ServiceImpl<AttendanceMapper, Attendance> implements IAttendanceService {

    @Resource
    private CourseMapper courseMapper;

    @Resource
    private StudentMapper studentMapper;

    @Override
    public AttendanceVO getAttendanceVO(Attendance attendance) {
        if (attendance == null) {
            return null;
        }
        AttendanceVO attendanceVO = BeanUtil.copyProperties(attendance, AttendanceVO.class);
        if (attendance.getCourseId() != null) {
            Course course = courseMapper.selectById(attendance.getCourseId());
            if (course != null) {
                attendanceVO.setCourseName(course.getName());
            }
        }
        if (attendance.getStudentId() != null) {
            Student student = studentMapper.selectById(attendance.getStudentId());
            if (student != null) {
                attendanceVO.setStudentName(student.getName());
            }
        }
        return attendanceVO;
    }

    @Override
    public LambdaQueryWrapper<Attendance> getQueryWrapper(Long courseId, Long studentId, LocalDate startDate, LocalDate endDate, Byte status) {
        LambdaQueryWrapper<Attendance> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotEmpty(courseId)) {
            queryWrapper.eq(Attendance::getCourseId, courseId);
        }
        if (ObjectUtil.isNotEmpty(studentId)) {
            queryWrapper.eq(Attendance::getStudentId, studentId);
        }
        if (ObjectUtil.isNotEmpty(startDate)) {
            queryWrapper.ge(Attendance::getAttendanceDate, startDate);
        }
        if (ObjectUtil.isNotEmpty(endDate)) {
            queryWrapper.le(Attendance::getAttendanceDate, endDate);
        }
        if (ObjectUtil.isNotEmpty(status)) {
            queryWrapper.eq(Attendance::getStatus, status);
        }
        queryWrapper.orderByDesc(Attendance::getAttendanceDate);
        return queryWrapper;
    }

    @Override
    public boolean checkIn(Long courseId, Long studentId, LocalDate date) {
        Course course = courseMapper.selectById(courseId);
        if (course == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "课程不存在");
        }
        Student student = studentMapper.selectById(studentId);
        if (student == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "学生不存在");
        }
        LambdaQueryWrapper<Attendance> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Attendance::getCourseId, courseId)
                .eq(Attendance::getStudentId, studentId)
                .eq(Attendance::getAttendanceDate, date);
        Attendance existing = this.getOne(wrapper);
        if (existing != null) {
            existing.setStatus((byte) 1);
            return this.updateById(existing);
        }
        Attendance attendance = new Attendance();
        attendance.setCourseId(courseId);
        attendance.setStudentId(studentId);
        attendance.setAttendanceDate(date);
        attendance.setStatus((byte) 1);
        return this.save(attendance);
    }

    @Override
    public boolean batchCheckIn(Long courseId, List<Long> studentIds, LocalDate date) {
        for (Long studentId : studentIds) {
            checkIn(courseId, studentId, date);
        }
        return true;
    }

    @Override
    public List<AttendanceVO> getAttendanceByCourse(Long courseId, LocalDate date) {
        LambdaQueryWrapper<Attendance> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Attendance::getCourseId, courseId);
        if (date != null) {
            wrapper.eq(Attendance::getAttendanceDate, date);
        }
        return this.list(wrapper).stream()
                .map(this::getAttendanceVO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AttendanceVO> getAttendanceByStudent(Long studentId, LocalDate startDate, LocalDate endDate) {
        LambdaQueryWrapper<Attendance> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Attendance::getStudentId, studentId);
        if (startDate != null) {
            wrapper.ge(Attendance::getAttendanceDate, startDate);
        }
        if (endDate != null) {
            wrapper.le(Attendance::getAttendanceDate, endDate);
        }
        return this.list(wrapper).stream()
                .map(this::getAttendanceVO)
                .collect(Collectors.toList());
    }
}
