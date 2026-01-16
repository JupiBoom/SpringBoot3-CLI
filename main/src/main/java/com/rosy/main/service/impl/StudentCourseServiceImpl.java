package com.rosy.main.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.main.domain.entity.StudentCourse;
import com.rosy.main.mapper.StudentCourseMapper;
import com.rosy.main.service.IStudentCourseService;
import org.springframework.stereotype.Service;

@Service
public class StudentCourseServiceImpl extends ServiceImpl<StudentCourseMapper, StudentCourse> implements IStudentCourseService {
}
