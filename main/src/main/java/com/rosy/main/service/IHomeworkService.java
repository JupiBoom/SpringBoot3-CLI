package com.rosy.main.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.common.domain.entity.IdRequest;
import com.rosy.main.domain.entity.Homework;
import com.rosy.main.domain.entity.HomeworkSubmission;

import java.math.BigDecimal;

public interface IHomeworkService extends IService<Homework> {

    Long addHomework(Homework homework);

    Boolean updateHomework(Homework homework);

    Boolean deleteHomework(IdRequest idRequest);

    Homework getHomeworkById(Long id);

    Page<Homework> listHomeworks(int current, int size, Long courseId);

    Long submitHomework(HomeworkSubmission submission);

    Boolean gradeHomework(Long submissionId, BigDecimal score, String feedback);

    Page<HomeworkSubmission> listSubmissions(int current, int size, Long homeworkId, Long studentId);
}