package com.rosy.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.main.domain.entity.CourseStudent;
import com.rosy.main.domain.entity.Student;
import com.rosy.main.mapper.CourseStudentMapper;
import com.rosy.main.mapper.StudentMapper;
import com.rosy.main.service.IStudentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements IStudentService {

    private final CourseStudentMapper courseStudentMapper;

    public StudentServiceImpl(CourseStudentMapper courseStudentMapper) {
        this.courseStudentMapper = courseStudentMapper;
    }

    @Override
    public Long addStudent(Student student) {
        this.save(student);
        return student.getId();
    }

    @Override
    public Boolean updateStudent(Student student) {
        return this.updateById(student);
    }

    @Override
    public Boolean deleteStudent(IdRequest idRequest) {
        return this.removeById(idRequest.getId());
    }

    @Override
    public Student getStudentById(Long id) {
        return this.getById(id);
    }

    @Override
    public Page<Student> listStudents(int current, int size, String studentNo, String name, String major) {
        Page<Student> page = new Page<>(current, size);
        LambdaQueryWrapper<Student> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(studentNo), Student::getStudentNo, studentNo);
        wrapper.like(StringUtils.isNotBlank(name), Student::getName, name);
        wrapper.eq(StringUtils.isNotBlank(major), Student::getMajor, major);
        wrapper.orderByDesc(Student::getCreateTime);
        return this.page(page, wrapper);
    }

    @Override
    public List<Student> getStudentsByCourseId(Long courseId) {
        LambdaQueryWrapper<CourseStudent> csWrapper = new LambdaQueryWrapper<>();
        csWrapper.eq(CourseStudent::getCourseId, courseId);
        csWrapper.eq(CourseStudent::getStatus, (byte) 1);
        List<Long> studentIds = courseStudentMapper.selectList(csWrapper)
                .stream().map(CourseStudent::getStudentId).collect(Collectors.toList());
        
        if (studentIds.isEmpty()) {
            return List.of();
        }
        return this.listByIds(studentIds);
    }
}