package com.rosy.main.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rosy.main.domain.entity.HomeworkSubmission;
import com.rosy.main.domain.vo.HomeworkSubmissionVO;

import java.util.List;

public interface IHomeworkSubmissionService extends IService<HomeworkSubmission> {

    HomeworkSubmissionVO getHomeworkSubmissionVO(HomeworkSubmission submission);

    LambdaQueryWrapper<HomeworkSubmission> getQueryWrapper(Long homeworkId, Long studentId);

    boolean submitHomework(Long homeworkId, Long studentId, String content, String fileUrl);

    boolean gradeHomework(Long submissionId, String score, String feedback);

    List<HomeworkSubmissionVO> getSubmissionsByHomework(Long homeworkId);

    List<HomeworkSubmissionVO> getSubmissionsByStudent(Long studentId);
}
