package com.rosy.main.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.main.domain.entity.Student;
import com.rosy.main.mapper.StudentMapper;
import com.rosy.main.service.IStudentService;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements IStudentService {
}
