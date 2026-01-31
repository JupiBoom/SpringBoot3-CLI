package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.main.domain.dto.course.CourseQueryRequest;
import com.rosy.main.domain.entity.Course;

import java.util.List;

public interface ICourseService extends IService<Course> {

    Long addCourse(Course course);

    Boolean updateCourse(Course course);

    Boolean deleteCourse(IdRequest idRequest);

    Course getCourseById(Long id);

    Page<Course> listCourses(int current, int size, String courseName, String semester);

    List<Course> getCoursesByTeacherId(Long teacherId);

    List<Course> getCoursesByTeacher(Long teacherId);

    LambdaQueryWrapper<Course> getQueryWrapper(CourseQueryRequest queryRequest);
}