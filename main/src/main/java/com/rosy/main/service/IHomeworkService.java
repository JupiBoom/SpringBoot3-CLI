package com.rosy.main.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.Homework;
import com.rosy.main.domain.vo.HomeworkVO;

import java.util.List;

public interface IHomeworkService extends IService<Homework> {

    HomeworkVO getHomeworkVO(Homework homework);

    LambdaQueryWrapper<Homework> getQueryWrapper(Long courseId, String title);

    boolean createHomework(Long courseId, String title, String description, String deadline);

    boolean updateHomework(Long id, String title, String description, String deadline);

    boolean deleteHomework(Long id);

    List<HomeworkVO> getHomeworksByCourse(Long courseId);
}
