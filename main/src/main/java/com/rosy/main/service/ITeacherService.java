package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.main.domain.entity.Teacher;

import java.util.List;
import java.util.Map;

public interface ITeacherService extends IService<Teacher> {

    Long addTeacher(Teacher teacher);

    Boolean updateTeacher(Teacher teacher);

    Boolean deleteTeacher(IdRequest idRequest);

    Teacher getTeacherById(Long id);

    Page<Teacher> listTeachers(int current, int size, String teacherNo, String name, String department);

    Map<String, Object> getTeacherStatistics(Long teacherId);
}