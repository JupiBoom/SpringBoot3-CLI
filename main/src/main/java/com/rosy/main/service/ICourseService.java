package com.rosy.main.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.Course;
import com.rosy.main.domain.vo.CourseVO;

public interface ICourseService extends IService<Course> {

    CourseVO getCourseVO(Course course);

    LambdaQueryWrapper<Course> getQueryWrapper(String name, Long teacherId, Byte status);

    CourseVO getCourseDetail(Long id);

    boolean addStudentToCourse(Long courseId, Long studentId);

    boolean removeStudentFromCourse(Long courseId, Long studentId);
}
