package com.rosy.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.main.domain.entity.Attendance;
import com.rosy.main.domain.entity.Course;
import com.rosy.main.mapper.TeacherMapper;
import com.rosy.main.domain.entity.Teacher;
import com.rosy.main.service.IAttendanceService;
import com.rosy.main.service.ITeacherService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements ITeacherService {

    private final CourseServiceImpl courseService;
    private final IAttendanceService attendanceService;

    public TeacherServiceImpl(CourseServiceImpl courseService, IAttendanceService attendanceService) {
        this.courseService = courseService;
        this.attendanceService = attendanceService;
    }

    @Override
    public Long addTeacher(Teacher teacher) {
        this.save(teacher);
        return teacher.getId();
    }

    @Override
    public Boolean updateTeacher(Teacher teacher) {
        return this.updateById(teacher);
    }

    @Override
    public Boolean deleteTeacher(IdRequest idRequest) {
        return this.removeById(idRequest.getId());
    }

    @Override
    public Teacher getTeacherById(Long id) {
        return this.getById(id);
    }

    @Override
    public Page<Teacher> listTeachers(int current, int size, String teacherNo, String name, String department) {
        Page<Teacher> page = new Page<>(current, size);
        LambdaQueryWrapper<Teacher> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(teacherNo), Teacher::getTeacherNo, teacherNo);
        wrapper.like(StringUtils.isNotBlank(name), Teacher::getName, name);
        wrapper.like(StringUtils.isNotBlank(department), Teacher::getDepartment, department);
        wrapper.orderByDesc(Teacher::getCreateTime);
        return this.page(page, wrapper);
    }

    @Override
    public Map<String, Object> getTeacherStatistics(Long teacherId) {
        Map<String, Object> statistics = new HashMap<>();
        List<Course> courses = courseService.getCoursesByTeacherId(teacherId);
        statistics.put("teacherId", teacherId);
        statistics.put("courseCount", courses.size());
        
        long totalClasses = 0;
        long totalStudents = 0;
        
        for (Course course : courses) {
            LambdaQueryWrapper<Attendance> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Attendance::getCourseId, course.getId());
            totalClasses += attendanceService.count(wrapper);
        }
        
        statistics.put("totalClasses", totalClasses);
        statistics.put("totalStudents", totalStudents);
        
        return statistics;
    }
}