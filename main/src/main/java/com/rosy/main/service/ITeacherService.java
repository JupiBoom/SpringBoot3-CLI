package com.rosy.main.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.Teacher;
import com.rosy.main.domain.vo.TeacherVO;

public interface ITeacherService extends IService<Teacher> {

    TeacherVO getTeacherVO(Teacher teacher);

    LambdaQueryWrapper<Teacher> getQueryWrapper(String name, String teacherNo, String department, Byte status);
}
