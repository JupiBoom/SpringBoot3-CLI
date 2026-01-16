package com.rosy.main.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.main.domain.entity.Course;
import com.rosy.main.mapper.CourseMapper;
import com.rosy.main.service.ICourseService;
import org.springframework.stereotype.Service;

@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements ICourseService {
}
