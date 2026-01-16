package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.main.domain.entity.Student;
import com.rosy.main.domain.vo.StudentVO;
import com.rosy.main.mapper.StudentMapper;
import com.rosy.main.service.IStudentService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements IStudentService {

    @Override
    public StudentVO getStudentVO(Student student) {
        return Optional.ofNullable(student)
                .map(s -> BeanUtil.copyProperties(s, StudentVO.class))
                .orElse(null);
    }

    @Override
    public LambdaQueryWrapper<Student> getQueryWrapper(String name, String studentNo, String major, Byte status) {
        LambdaQueryWrapper<Student> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotEmpty(name)) {
            queryWrapper.like(Student::getName, name);
        }
        if (ObjectUtil.isNotEmpty(studentNo)) {
            queryWrapper.eq(Student::getStudentNo, studentNo);
        }
        if (ObjectUtil.isNotEmpty(major)) {
            queryWrapper.like(Student::getMajor, major);
        }
        if (ObjectUtil.isNotEmpty(status)) {
            queryWrapper.eq(Student::getStatus, status);
        }
        queryWrapper.orderByDesc(Student::getCreateTime);
        return queryWrapper;
    }
}
