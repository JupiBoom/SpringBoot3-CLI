package com.rosy.course.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.course.domain.entity.Course;
import com.rosy.course.mapper.CourseMapper;
import com.rosy.course.service.ICourseService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 课程表 Service 实现类
 * </p>
 *
 * @author Rosy
 * @since 2025-01-16
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements ICourseService {

}