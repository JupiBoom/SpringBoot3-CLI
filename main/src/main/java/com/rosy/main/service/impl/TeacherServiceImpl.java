package com.rosy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rosy.main.domain.entity.Teacher;
import com.rosy.main.domain.vo.TeacherVO;
import com.rosy.main.mapper.TeacherMapper;
import com.rosy.main.service.ITeacherService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements ITeacherService {

    @Override
    public TeacherVO getTeacherVO(Teacher teacher) {
        return Optional.ofNullable(teacher)
                .map(t -> BeanUtil.copyProperties(t, TeacherVO.class))
                .orElse(null);
    }

    @Override
    public LambdaQueryWrapper<Teacher> getQueryWrapper(String name, String teacherNo, String department, Byte status) {
        LambdaQueryWrapper<Teacher> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotEmpty(name)) {
            queryWrapper.like(Teacher::getName, name);
        }
        if (ObjectUtil.isNotEmpty(teacherNo)) {
            queryWrapper.eq(Teacher::getTeacherNo, teacherNo);
        }
        if (ObjectUtil.isNotEmpty(department)) {
            queryWrapper.like(Teacher::getDepartment, department);
        }
        if (ObjectUtil.isNotEmpty(status)) {
            queryWrapper.eq(Teacher::getStatus, status);
        }
        queryWrapper.orderByDesc(Teacher::getCreateTime);
        return queryWrapper;
    }
}
