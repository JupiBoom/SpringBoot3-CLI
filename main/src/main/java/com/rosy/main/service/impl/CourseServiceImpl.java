package com.rosy.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.main.domain.entity.Course;
import com.rosy.main.mapper.CourseMapper;
import com.rosy.main.service.ICourseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements ICourseService {

    @Override
    public Long addCourse(Course course) {
        this.save(course);
        return course.getId();
    }

    @Override
    public Boolean updateCourse(Course course) {
        return this.updateById(course);
    }

    @Override
    public Boolean deleteCourse(IdRequest idRequest) {
        return this.removeById(idRequest.getId());
    }

    @Override
    public Course getCourseById(Long id) {
        return this.getById(id);
    }

    @Override
    public Page<Course> listCourses(int current, int size, String courseName, String semester) {
        Page<Course> page = new Page<>(current, size);
        LambdaQueryWrapper<Course> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(courseName), Course::getCourseName, courseName);
        wrapper.eq(StringUtils.isNotBlank(semester), Course::getSemester, semester);
        wrapper.orderByDesc(Course::getCreateTime);
        return this.page(page, wrapper);
    }

    @Override
    public List<Course> getCoursesByTeacherId(Long teacherId) {
        LambdaQueryWrapper<Course> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Course::getTeacherId, teacherId);
        wrapper.eq(Course::getStatus, (byte) 1);
        return this.list(wrapper);
    }
}