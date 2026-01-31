package com.rosy.main.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.main.domain.dto.student.StudentQueryRequest;
import com.rosy.main.domain.entity.Student;

import java.util.List;

public interface IStudentService extends IService<Student> {

    Long addStudent(Student student);

    Boolean updateStudent(Student student);

    Boolean deleteStudent(IdRequest idRequest);

    Student getStudentById(Long id);

    Page<Student> listStudents(int current, int size, String studentNo, String name, String major);

    List<Student> getStudentsByCourseId(Long courseId);

    List<Student> getStudentsByCourse(Long courseId);

    LambdaQueryWrapper<Student> getQueryWrapper(StudentQueryRequest queryRequest);
}