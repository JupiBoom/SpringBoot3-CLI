package com.rosy.main.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.Student;
import com.rosy.main.domain.vo.StudentVO;

public interface IStudentService extends IService<Student> {

    StudentVO getStudentVO(Student student);

    LambdaQueryWrapper<Student> getQueryWrapper(String name, String studentNo, String major, Byte status);
}
